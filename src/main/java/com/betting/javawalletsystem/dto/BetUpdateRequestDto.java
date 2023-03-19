package com.betting.javawalletsystem.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetUpdateRequestDto extends TransactionRequestDto{
    @NotNull
    @Positive
    private Long betTransactionId;

    public BetUpdateRequestDto(Long transactionId, Long playerId, BigDecimal amount, Long betTransactionId) {
        super(transactionId, playerId, amount);
        this.betTransactionId = betTransactionId;
    }
}
