package com.betting.javawalletsystem.repository;

import com.betting.javawalletsystem.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
