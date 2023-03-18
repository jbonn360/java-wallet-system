package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.BetUpdateRequestDto;
import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.exception.InsufficientFundsException;
import com.betting.javawalletsystem.exception.InvalidTransactionException;
import com.betting.javawalletsystem.exception.ObjectNotFoundException;
import com.betting.javawalletsystem.mappers.TransactionMapper;
import com.betting.javawalletsystem.model.*;
import com.betting.javawalletsystem.repository.BetRepository;
import com.betting.javawalletsystem.repository.PlayerRepository;
import com.betting.javawalletsystem.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BetServiceImpl implements BetService{
    private final PlayerRepository playerRepository;
    private final WalletRepository walletRepository;
    private final BetRepository betRepository;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public BetServiceImpl(@Autowired PlayerRepository playerRepository,
                          @Autowired WalletRepository walletRepository,
                          @Autowired BetRepository betRepository,
                          @Autowired TransactionService transactionService,
                          @Autowired TransactionMapper transactionMapper){
        this.playerRepository = playerRepository;
        this.walletRepository = walletRepository;
        this.betRepository = betRepository;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    @Override
    public TransactionResponseDto processBet(TransactionRequestDto betRequest) {
        if(betRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionException(
                    String.format("Cannot place bet with amount %d", betRequest.getAmount())
            );

        Optional<Transaction> transactionOpt = transactionService.getAndCheckTransaction(
                betRequest, TransactionType.BET_PLACEMENT);

        final Transaction transaction = transactionOpt.orElseGet(() -> placeBet(betRequest));

        final TransactionResponseDto betPlacementResponse = transactionMapper
                .transactionModelToTransactionResponseDto(transaction);

        return betPlacementResponse;

    }

    private Transaction placeBet(TransactionRequestDto betRequest) {
        // get player from database
        Player player = playerRepository.findById(betRequest.getPlayerId()).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id %d not found",
                        betRequest.getPlayerId())
                ));

        Wallet wallet = player.getWallet();

        // check that player affords bet
        if(wallet.getCombinedBalance().compareTo(betRequest.getAmount()) < 0)
            throw new InsufficientFundsException("Player does not have enough funds to place bet");

        BigDecimal startingCashBalance = wallet.getCashBalance();
        BigDecimal startingBonusBalance = wallet.getBonusBalance();

        // deduct funds from player's wallet
        //  deduct primarily from cash balance until this is zeo
        //  if cash is zero, deduct from bonus balance
        if(betRequest.getAmount().compareTo(wallet.getCashBalance()) > 0){
            BigDecimal betBalance = betRequest.getAmount();
            betBalance = betBalance.subtract(wallet.getCashBalance());
            wallet.setCashBalance(BigDecimal.ZERO);
            wallet.setBonusBalance(wallet.getBonusBalance().subtract(betBalance));
        }else
            wallet.setCashBalance(wallet.getCashBalance().subtract(betRequest.getAmount()));

        wallet = walletRepository.save(wallet);
        player.setWallet(wallet);

        //create and save bet to database
        Bet bet = Bet.builder()
                .transactionId(betRequest.getTransactionId())
                .cashAmount(startingCashBalance.subtract(wallet.getCashBalance()))
                .bonusAmount(startingBonusBalance.subtract(wallet.getBonusBalance()))
                .betStatus(BetStatus.PLACED)
                .player(player).build();
        bet = betRepository.save(bet);

        //create new transaction object in db
        Transaction transaction = transactionService.saveTransaction(player, bet.getCashAmount(),
                bet.getBonusAmount(), betRequest.getTransactionId(), TransactionType.BET_PLACEMENT);

        return transaction;
    }

    @Transactional
    @Override
    public TransactionResponseDto updateBetStatus(BetUpdateRequestDto betUpdateRequest) {
        // validate win amount
        if(betUpdateRequest.getAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidTransactionException(
                    String.format("Cannot set bet outcome amount to %d", betUpdateRequest.getAmount())
            );

        // deduce transaction type from amout won
        final TransactionType transactionType = betUpdateRequest.getAmount().equals(BigDecimal.ZERO) ?
                TransactionType.BET_LOSS : TransactionType.BET_WIN;

        // check for prior transactions
        Optional<Transaction> transactionOpt = transactionService.getAndCheckTransaction(
                betUpdateRequest, transactionType);

        final Transaction transaction = transactionOpt.orElseGet(() -> updateBet(betUpdateRequest, transactionType));

        final TransactionResponseDto betPlacementResponse = transactionMapper
                .transactionModelToTransactionResponseDto(transaction);

        return betPlacementResponse;
    }

    private Transaction updateBet(BetUpdateRequestDto betUpdateRequest, TransactionType transactionType) {
        //deduce bet status
        final BetStatus betStatus = betUpdateRequest.getAmount().equals(BigDecimal.ZERO) ?
                BetStatus.LOST : BetStatus.WON;

        // update bet status
        Bet bet = betRepository.findByTransactionId(betUpdateRequest.getBetTransactionId()).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Bet with transaction id %d was not found",
                        betUpdateRequest.getBetTransactionId())
                )
        );

        bet.setBetStatus(betStatus);
        bet = betRepository.save(bet);

        // reward player (if won)
        // 1. get player from database
        Player player = playerRepository.findById(betUpdateRequest.getPlayerId()).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id %d not found",
                        betUpdateRequest.getPlayerId())
                ));

        BigDecimal startingCashBalance = player.getWallet().getCashBalance();
        BigDecimal startingBonusBalance = player.getWallet().getBonusBalance();

        // 2. get wallet and update balance if won
        if(betStatus == BetStatus.WON)
            player.setWallet(handleBetWin(player, bet, betUpdateRequest));

        //create new transaction object in db
        BigDecimal transactionCashAmount = player.getWallet().getCashBalance().subtract(startingCashBalance);
        BigDecimal transactionBonusAmount = player.getWallet().getBonusBalance().subtract(startingBonusBalance);

        Transaction transaction = transactionService.saveTransaction(player, transactionCashAmount,
                transactionBonusAmount, betUpdateRequest.getTransactionId(), transactionType);

        return transaction;
    }

    private Wallet handleBetWin(Player player, Bet bet, BetUpdateRequestDto betUpdateRequest){
        Wallet wallet = player.getWallet();
        if(bet.getCashAmount().equals(BigDecimal.ZERO)) // pure cash bet
            wallet.setBonusBalance(wallet.getBonusBalance().add(betUpdateRequest.getAmount()));
        else if (bet.getBonusAmount().equals(BigDecimal.ZERO)) // pure bonus bet
            wallet.setCashBalance(wallet.getCashBalance().add(betUpdateRequest.getAmount()));
        else{
            // mixed cash / bonus bet win
            final double cashFraction =
                    bet.getCashAmount().doubleValue() / bet.getCombinedAmount().doubleValue();
            final double bonusFraction =
                    bet.getCashAmount().doubleValue() / bet.getCombinedAmount().doubleValue();

            final  BigDecimal cashWon =
                    betUpdateRequest.getAmount().multiply(BigDecimal.valueOf(cashFraction));
            final BigDecimal bonusWon =
                    betUpdateRequest.getAmount().multiply(BigDecimal.valueOf(bonusFraction));

            wallet.setCashBalance(wallet.getCashBalance().add(cashWon));
            wallet.setBonusBalance(wallet.getBonusBalance().add(bonusWon));
        }

        return walletRepository.save(wallet);
    }
}
