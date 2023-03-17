package com.betting.javawalletsystem.repository;

import com.betting.javawalletsystem.model.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
