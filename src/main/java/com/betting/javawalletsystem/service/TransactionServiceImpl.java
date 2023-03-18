package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(@Autowired TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }


    @Override
    public Transaction saveTransaction(Player player, BigDecimal cashAmount, BigDecimal bonusAmount,
                                       Long transactionId, TransactionType transactionType) {
        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .transactionDt(Instant.now())
                .player(player)
                .bonusBalanceAfter(player.getWallet().getBonusBalance())
                .cashBalanceAfter(player.getWallet().getCashBalance())
                .bonusAmount(bonusAmount) // todo: implement bonus balance feature
                .cashAmount(cashAmount)
                .type(transactionType).build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionByTransactionId(Long transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }
}
