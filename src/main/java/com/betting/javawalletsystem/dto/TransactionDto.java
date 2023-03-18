package com.betting.javawalletsystem.dto;

import com.betting.javawalletsystem.model.TransactionType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionDto {
    @NotNull
    private Long transactionId;

    @NotNull
    private Instant transactionDt;

    @NotNull
    private BigDecimal cashAmount;

    @NotNull
    private BigDecimal cashBalanceAfter;

    @NotNull
    private BigDecimal bonusAmount;

    @NotNull
    private BigDecimal bonusBalanceAfter;

    @NotNull
    private TransactionType type;

    @NotNull
    private Long playerId;

    @NotNull
    private String player;
}
