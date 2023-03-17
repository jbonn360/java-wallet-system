package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.DepositRequestDto;
import com.betting.javawalletsystem.dto.DepositResponseDto;

public interface FundsService {
    DepositResponseDto processDeposit(DepositRequestDto depositRequest);
}
