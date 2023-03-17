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
public class DepositResponseDto {
    @NotNull
    @Positive
    public Long transactionId;

    @NotNull
    @Positive
    public Long playerId;

    @NotNull
    @Positive
    public BigDecimal cashBalance;

    @NotNull
    @PositiveOrZero
    public BigDecimal bonusBalance;
}
