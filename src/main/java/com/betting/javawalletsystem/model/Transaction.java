package com.betting.javawalletsystem.model;

import com.sun.istack.NotNull;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

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
}
