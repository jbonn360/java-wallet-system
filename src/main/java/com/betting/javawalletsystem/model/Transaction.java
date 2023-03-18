package com.betting.javawalletsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(unique = true)
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
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;

    @NotNull
    @ManyToOne
    private Player player;

    public BigDecimal getCombinedAmount() {
        return cashAmount.add(bonusAmount);
    }
}
