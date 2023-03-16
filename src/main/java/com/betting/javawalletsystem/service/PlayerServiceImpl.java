package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.exception.UserNotFoundException;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(@Autowired PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    @Override
    public Player findPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %d was not found.", id))
        );
    }
}
