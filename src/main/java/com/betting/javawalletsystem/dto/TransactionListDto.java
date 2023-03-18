package com.betting.javawalletsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
public class TransactionListDto {
    @NotNull
    private List<TransactionDto> transactionList;
}
