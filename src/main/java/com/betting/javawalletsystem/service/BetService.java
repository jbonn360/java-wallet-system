package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.BetUpdateRequestDto;
import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;

public interface BetService {
    TransactionResponseDto processBet(TransactionRequestDto betRequest);

    TransactionResponseDto updateBetStatus(BetUpdateRequestDto winRequest);
}
