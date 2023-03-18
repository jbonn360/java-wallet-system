package com.betting.javawalletsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private Long transactionId;

    @NotNull
    private BigDecimal cashAmount;

    @NotNull
    private BigDecimal bonusAmount;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private BetStatus betStatus;

    @NotNull
    @OneToOne
    private Player player;

    public BigDecimal getCombinedAmount(){
        return cashAmount.add(bonusAmount);
    }
}
