package com.betting.javawalletsystem.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {
    @NotNull
    @Positive
    private Long transactionId;

    @NotNull
    @Positive
    private Long playerId;

    @NotNull
    @Positive
    private BigDecimal cashBalance;

    @NotNull
    @PositiveOrZero
    private BigDecimal bonusBalance;
}
