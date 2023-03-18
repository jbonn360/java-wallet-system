package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.exception.InsufficientFundsException;
import com.betting.javawalletsystem.exception.InvalidTransactionException;
import com.betting.javawalletsystem.exception.ObjectNotFoundException;
import com.betting.javawalletsystem.mappers.TransactionMapper;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.PlayerRepository;
import com.betting.javawalletsystem.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FundsServiceImpl implements FundsService{
    private final PlayerRepository playerRepository;
    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final BigDecimal bonusMinThreshold;

    public FundsServiceImpl(@Autowired PlayerRepository playerRepository,
                            @Autowired WalletRepository walletRepository,
                            @Autowired TransactionService transactionService,
                            @Value("${app.deposit.bonus-min-threshold}") BigDecimal bonusMinThreshold,
                             @Autowired TransactionMapper transactionMapper){
        this.playerRepository = playerRepository;
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.bonusMinThreshold = bonusMinThreshold;
    }

    @Transactional
    @Override
    public TransactionResponseDto processDeposit(TransactionRequestDto depositRequest) {
        if(depositRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionException(
                    String.format("Cannot process deposit with amount %d", depositRequest.getAmount())
            );

        Optional<Transaction> transactionOpt = transactionService.getAndCheckTransaction(
                depositRequest, TransactionType.DEPOSIT);

        final Transaction transaction = transactionOpt.orElseGet(() -> createDepositTransaction(depositRequest));

        final TransactionResponseDto depositResponse = transactionMapper
                .transactionModelToTransactionResponseDto(transaction);

        return depositResponse;
    }

    private Transaction createDepositTransaction(TransactionRequestDto depositRequest){
        // get player from database
        Player player = playerRepository.findById(depositRequest.getPlayerId()).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id %d not found",
                        depositRequest.getPlayerId())
                ));

        // add funds to player's wallet
        Wallet wallet = player.getWallet();
        wallet.setCashBalance(wallet.getCashBalance().add(depositRequest.getAmount()));

        // if deposit amount is greater than or equal to bonus min threshold (defaults to 100)
        // award 100% bonus balance
        BigDecimal bonusAmount = BigDecimal.ZERO;
        if(depositRequest.getAmount().compareTo(bonusMinThreshold) >= 0){
            bonusAmount = depositRequest.getAmount();
            wallet.setBonusBalance(wallet.getBonusBalance().add(bonusAmount));
        }

        // update wallet balance
        wallet = walletRepository.save(wallet);
        player.setWallet(wallet);

        //create new transaction object in db
        Transaction transaction = transactionService.saveTransaction(player, depositRequest.getAmount(),
                bonusAmount, depositRequest.getTransactionId(), TransactionType.DEPOSIT);

        return transaction;
    }

    @Transactional
    @Override
    public TransactionResponseDto processWithdrawal(TransactionRequestDto withdrawalRequest) {
        if(withdrawalRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionException(
                    String.format("Cannot process withdrawal with amount %d", withdrawalRequest.getAmount())
            );

        Optional<Transaction> transactionOpt = transactionService.getAndCheckTransaction(
                withdrawalRequest, TransactionType.WITHDRAWAL);

        final Transaction transaction = transactionOpt.orElseGet(
                () -> createWithdrawalTransaction(withdrawalRequest));

        final TransactionResponseDto depositResponse = transactionMapper
                .transactionModelToTransactionResponseDto(transaction);

        return depositResponse;
    }

    private Transaction createWithdrawalTransaction(TransactionRequestDto withdrawalRequest) {
        // get player from database
        Player player = playerRepository.findById(withdrawalRequest.getPlayerId()).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id %d not found",
                        withdrawalRequest.getPlayerId())
                ));

        if(player.getWallet().getCashBalance().compareTo(withdrawalRequest.getAmount()) < 0)
            throw new InsufficientFundsException("player does not have enough funds to perform withdrawal");

        // remove funds from player's wallet
        Wallet wallet = player.getWallet();
        wallet.setCashBalance(wallet.getCashBalance().subtract(withdrawalRequest.getAmount()));

        // update wallet balance
        wallet = walletRepository.save(wallet);
        player.setWallet(wallet);

        //create new transaction object in db
        Transaction transaction = transactionService.saveTransaction(player, withdrawalRequest.getAmount(),
                BigDecimal.ZERO, withdrawalRequest.getTransactionId(), TransactionType.WITHDRAWAL);

        return transaction;
    }
}
