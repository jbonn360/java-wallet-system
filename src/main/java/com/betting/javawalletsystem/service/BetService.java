package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;

public interface BetService {
    public TransactionResponseDto processBet(TransactionRequestDto betRequest);
}
