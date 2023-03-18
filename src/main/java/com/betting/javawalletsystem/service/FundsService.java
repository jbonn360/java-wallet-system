package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;

public interface FundsService {
    TransactionResponseDto processDeposit(TransactionRequestDto depositRequest);

    TransactionResponseDto processWithdrawal(TransactionRequestDto withdrawalRequest);
}
