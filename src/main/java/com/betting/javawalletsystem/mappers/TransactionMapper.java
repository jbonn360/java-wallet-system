package com.betting.javawalletsystem.mappers;

import com.betting.javawalletsystem.dto.TransactionDto;
import com.betting.javawalletsystem.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {
    @Mapping(target = "player", source = "transactionModel.player.username")
    TransactionDto transactionModelTotransactionDto(Transaction transactionModel);
}
