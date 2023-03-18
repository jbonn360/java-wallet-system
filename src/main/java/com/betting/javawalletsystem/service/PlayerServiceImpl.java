package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.WalletDto;
import com.betting.javawalletsystem.exception.PlayerNotFoundException;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Wallet;
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
                () -> new PlayerNotFoundException(String.format("User with id %d was not found.", id))
        );
    }

    @Override
    public WalletDto getWalletDetails(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new PlayerNotFoundException(String.format("Player with id %d was not found", playerId))
        );

        WalletDto walletDto = WalletDto.builder()
                .bonusBalance(player.getWallet().getBonusBalance())
                .cashBalance(player.getWallet().getCashBalance())
                .username(player.getUsername())
                .build();

        return walletDto;
    }
}
