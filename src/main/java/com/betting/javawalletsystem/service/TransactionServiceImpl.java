package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(@Autowired TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }


    @Override
    public Transaction saveTransaction(Wallet wallet, BigDecimal cashAmount, BigDecimal bonusAmount,
                                       TransactionType transactionType) {
        Transaction transaction = Transaction.builder()
                .transactionDt(Instant.now())
                .bonusBalanceAfter(wallet.getBonusBalance())
                .cashBalanceAfter(wallet.getCashBalance())
                .bonusAmount(bonusAmount) // todo: implement bonus balance feature
                .cashAmount(cashAmount)
                .type(transactionType).build();

        return transactionRepository.save(transaction);
    }
}
