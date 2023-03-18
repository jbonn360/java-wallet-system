package com.betting.javawalletsystem.repository;

import com.betting.javawalletsystem.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(Long transactionId);

    List<Transaction> findAllByOrderByTransactionDtDesc(Pageable pageable);
}
