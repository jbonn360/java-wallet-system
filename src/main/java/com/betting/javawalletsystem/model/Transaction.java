package com.betting.javawalletsystem.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;
}
