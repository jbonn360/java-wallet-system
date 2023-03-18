package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.WalletDto;
import com.betting.javawalletsystem.model.Player;

public interface PlayerService {
    Player findPlayerById(Long id);

    WalletDto getWalletDetails(Long playerId);
}
