package com.betting.javawalletsystem.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDto {
    @NotNull
    private String username;

    @PositiveOrZero
    @NotNull
    private BigDecimal cashBalance;

    @PositiveOrZero
    @NotNull
    private BigDecimal bonusBalance;
}
