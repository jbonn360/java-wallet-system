package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction saveTransaction(Wallet wallet, BigDecimal cashAmount, BigDecimal bonusAmount,
                                TransactionType transactionType);
}
