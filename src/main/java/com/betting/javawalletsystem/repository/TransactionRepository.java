package com.betting.javawalletsystem.repository;

import com.betting.javawalletsystem.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(Long transactionId);
}
