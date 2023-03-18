package com.betting.javawalletsystem.repository;

import com.betting.javawalletsystem.model.Bet;
import com.betting.javawalletsystem.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BetRepository extends CrudRepository<Bet, Long> {
    Optional<Bet> findByTransactionId(Long transactionId);
}
