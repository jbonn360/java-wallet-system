package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionListDto;
import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionService {
    Transaction saveTransaction(Player player, BigDecimal cashAmount, BigDecimal bonusAmount,
                                Long transactionId, TransactionType transactionType);

    TransactionListDto getAllTransactions(int limit, int offset);

    Optional<Transaction> getAndCheckTransaction(TransactionRequestDto transactionRequest,
                                                 TransactionType transactionType);
}
