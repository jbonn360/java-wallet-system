package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.exception.InsufficientFundsException;
import com.betting.javawalletsystem.exception.InvalidTransactionException;
import com.betting.javawalletsystem.exception.PlayerNotFoundException;
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
                () -> new PlayerNotFoundException(String.format("User with id %d not found",
                        betRequest.getPlayerId())
                ));

        Wallet wallet = player.getWallet();

        // check that player affords bet
        if(wallet.getCombinedBalance().compareTo(betRequest.getAmount()) < 0)
            throw new InsufficientFundsException("Player does not have enough funds");

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
}
