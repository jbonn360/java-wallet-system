package com.betting.javawalletsystem.mappers;

import com.betting.javawalletsystem.dto.TransactionDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {
    @Mapping(target = "player", source = "transactionModel.player.username")
    @Mapping(target = "playerId", source = "transactionModel.player.id")
    TransactionDto transactionModelTotransactionDto(Transaction transactionModel);

    @Mapping(target = "playerId", source = "transactionModel.player.id")
    @Mapping(target = "cashBalance", source = "transactionModel.cashBalanceAfter")
    @Mapping(target = "bonusBalance", source = "transactionModel.bonusBalanceAfter")
    TransactionResponseDto transactionModelToTransactionResponseDto(Transaction transactionModel);
}
