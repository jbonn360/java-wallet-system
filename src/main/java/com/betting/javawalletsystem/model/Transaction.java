package com.betting.javawalletsystem.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant transactionDt;

    private BigDecimal cashAmount;

    private BigDecimal cashBalanceAfter;

    private BigDecimal bonusAmount;

    private BigDecimal bonusBalanceAfter;
}
