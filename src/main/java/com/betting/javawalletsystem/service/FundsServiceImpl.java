package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.DepositRequestDto;
import com.betting.javawalletsystem.dto.DepositResponseDto;
import com.betting.javawalletsystem.exception.TransactionExistsException;
import com.betting.javawalletsystem.exception.UserNotFoundException;
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
    private final BigDecimal bonusMinThreshold;

    public FundsServiceImpl(@Autowired PlayerRepository playerRepository,
                            @Autowired WalletRepository walletRepository,
                            @Autowired TransactionService transactionService,
                            @Value("${app.deposit.bonus-min-threshold}") BigDecimal bonusMinThreshold){
        this.playerRepository = playerRepository;
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
        this.bonusMinThreshold = bonusMinThreshold;
    }

    @Transactional
    @Override
    public DepositResponseDto processDeposit(DepositRequestDto depositRequest) {
        Optional<Transaction> transactionOpt = transactionService.getTransactionByTransactionId(
                depositRequest.getTransactionId());

        // if transactionOpt exists but amount or user is different
        if(transactionOpt.isPresent()){
            if(transactionOpt.get().getCashAmount().compareTo(depositRequest.getAmount()) != 0)
                throw new TransactionExistsException(
                        String.format("Transaction with id %d already exists but amount is different",
                                depositRequest.getTransactionId()));
            else if(transactionOpt.get().getPlayer().getId() != depositRequest.getPlayerId())
                throw new TransactionExistsException(
                        String.format("Transaction with id %d already exists but user is different",
                                depositRequest.getTransactionId()));
        }

        final Transaction transaction = transactionOpt.orElseGet(() -> carryOutTransaction(depositRequest));

        // return response dto representing the transactionOpt
        DepositResponseDto depositResponse = DepositResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .playerId(transaction.getPlayer().getId())
                .cashBalance(transaction.getCashBalanceAfter())
                .bonusBalance(transaction.getBonusBalanceAfter()).build();

        return depositResponse;
    }

    private Transaction carryOutTransaction(DepositRequestDto depositRequest){
        // get player from database
        Player player = playerRepository.findById(depositRequest.getPlayerId()).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %d not found",
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
}
