package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionDto;
import com.betting.javawalletsystem.dto.TransactionListDto;
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
                .bonusAmount(bonusAmount) // todo: implement bonus balance feature
                .cashAmount(cashAmount)
                .type(transactionType).build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionByTransactionId(Long transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
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
