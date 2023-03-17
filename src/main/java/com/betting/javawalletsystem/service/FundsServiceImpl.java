package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.DepositRequestDto;
import com.betting.javawalletsystem.dto.DepositResponseDto;
import com.betting.javawalletsystem.exception.UserNotFoundException;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.Transaction;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.PlayerRepository;
import com.betting.javawalletsystem.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FundsServiceImpl implements FundsService{
    private final PlayerRepository playerRepository;
    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final BigDecimal bonusMinThreshold;

    public FundsServiceImpl(@Autowired PlayerRepository playerRepository,
                            @Autowired WalletRepository walletRepository,
                            @Autowired TransactionService transactionService,
                            @Value("${app.deposit.bonus-min-threshold}") BigDecimal bonusMinThreshold){
        this.playerRepository = playerRepository;
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
        this.bonusMinThreshold = bonusMinThreshold;
    }

    @Transactional
    @Override
    public DepositResponseDto processDeposit(DepositRequestDto depositRequest) {
        // todo: check that player id corresponds to the currently authenticated player
        // get player from database
        Player player = playerRepository.findById(depositRequest.playerId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %d not found",
                        depositRequest.playerId)
        ));

        // add funds to player's wallet
        Wallet wallet = player.getWallet();
        wallet.setCashBalance(wallet.getCashBalance().add(depositRequest.getAmount()));

        // if deposit amount is greater than or equal to bonus min threshold (defaults to 100)
        // award 100% bonus balance
        BigDecimal bonusAmount = BigDecimal.ZERO;
        if(depositRequest.getAmount().compareTo(bonusMinThreshold) >= 0){
            bonusAmount = depositRequest.getAmount();
            wallet.setBonusBalance(wallet.getBonusBalance().add(bonusAmount));
        }

        wallet = walletRepository.save(wallet);

        //create new transaction object in db
        Transaction transaction = transactionService.saveTransaction(wallet, depositRequest.amount,
                bonusAmount, TransactionType.DEPOSIT);

        // return response dto representing the transaction
        DepositResponseDto depositResponse = DepositResponseDto.builder()
                .transactionId(depositRequest.transactionId)
                .playerId(depositRequest.playerId)
                .cashBalance(transaction.getCashBalanceAfter())
                .bonusBalance(transaction.getBonusBalanceAfter()).build();

        return depositResponse;
    }
}
