package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionDto;
import com.betting.javawalletsystem.dto.TransactionListDto;
import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.exception.TransactionExistsException;
import com.betting.javawalletsystem.mappers.TransactionMapper;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(@Autowired TransactionRepository transactionRepository,
                                  @Autowired TransactionMapper transactionMapper){
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Transaction saveTransaction(Player player, BigDecimal cashAmount, BigDecimal bonusAmount,
                                       Long transactionId, TransactionType transactionType) {
        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .transactionDt(Instant.now())
                .player(player)
                .bonusBalanceAfter(player.getWallet().getBonusBalance())
                .cashBalanceAfter(player.getWallet().getCashBalance())
                .bonusAmount(bonusAmount)
                .cashAmount(cashAmount)
                .type(transactionType).build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getAndCheckTransaction(TransactionRequestDto transactionRequest,
                                                        TransactionType transactionType) {
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(
                transactionRequest.getTransactionId());

        // if transactionOpt exists but amount or user or type is different
        if(transactionOpt.isPresent()){
            if(transactionOpt.get().getPlayer().getId() != transactionRequest.getPlayerId())
                throw new TransactionExistsException(
                        String.format("Transaction with id %d already exists but player is different",
                                transactionRequest.getTransactionId()));
            else if(transactionOpt.get().getType() != transactionType)
                throw new TransactionExistsException(
                        String.format("Transaction with id %d already exists but transaction type is different",
                                transactionRequest.getTransactionId()));
            else{
                if(transactionType == TransactionType.BET_PLACEMENT ||  transactionType == TransactionType.BET_WIN ||
                        transactionType == TransactionType.BET_LOSS)
                {
                    if(transactionOpt.get().getCombinedAmount().compareTo(transactionRequest.getAmount()) != 0)
                        throw new TransactionExistsException(
                                String.format("Transaction with id %d already exists but amount is different",
                                        transactionRequest.getTransactionId()));
                } else{
                    if(transactionOpt.get().getCashAmount().compareTo(transactionRequest.getAmount()) != 0)
                        throw new TransactionExistsException(
                                String.format("Transaction with id %d already exists but amount is different",
                                        transactionRequest.getTransactionId()));
                }
            }
        }

        return transactionOpt;
    }

    @Override
    public TransactionListDto getAllTransactions(int limit, int offset) {
        List<Transaction> transactionList =
                transactionRepository.findAllByOrderByTransactionDtDesc(PageRequest.of(offset, limit));

        // map transaction model to transaction dto
        List<TransactionDto> transactions = transactionList.stream().map(
                transactionMapper::transactionModelTotransactionDto
        ).collect(Collectors.toList());

        TransactionListDto result = new TransactionListDto(transactions);

        return result;
    }
}
