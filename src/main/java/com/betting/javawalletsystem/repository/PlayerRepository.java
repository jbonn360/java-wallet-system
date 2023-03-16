package com.betting.javawalletsystem.repository;

import com.betting.javawalletsystem.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> findByUsername(String username);
}
