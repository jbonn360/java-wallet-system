package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.exception.InsufficientFundsException;
import com.betting.javawalletsystem.mappers.TransactionMapper;
import com.betting.javawalletsystem.mappers.TransactionMapperImpl;
import com.betting.javawalletsystem.model.Player;
import com.betting.javawalletsystem.model.TransactionType;
import com.betting.javawalletsystem.model.Wallet;
import com.betting.javawalletsystem.repository.PlayerRepository;
import com.betting.javawalletsystem.repository.TransactionRepository;
import com.betting.javawalletsystem.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FundsServiceImplTests {
    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final WalletRepository walletRepository = mock(WalletRepository.class);
    private final TransactionMapper transactionMapper = mock(TransactionMapperImpl.class);
    private final BigDecimal bonusMinThreshold = new BigDecimal(100);
    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    private final TransactionService transactionService = new TransactionServiceImpl(
            transactionRepository, transactionMapper
    );

    private final FundsServiceImpl fundService = new FundsServiceImpl(
            playerRepository, walletRepository, spy(transactionService), bonusMinThreshold, transactionMapper
    );

    @Test
    public void givenValidDepositRequest_WhenProcessDeposit_ThenDepositFunds(){
        //given
        TransactionRequestDto depositRequest = TransactionRequestDto.builder()
                .amount(new BigDecimal(100))
                .playerId(1L)
                .transactionId(1L).build();

        Wallet wallet = Wallet.builder().bonusBalance(new BigDecimal(0))
                .cashBalance(new BigDecimal(100)).build();

        Player player = Player.builder().id(1L) .username("testuser").wallet(wallet).build();

        //when
        when(transactionService.getAndCheckTransaction(depositRequest, TransactionType.DEPOSIT))
                .thenReturn(Optional.empty());

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        when(transactionMapper.transactionModelToTransactionResponseDto(any()))
                .thenCallRealMethod();

        //then
        TransactionResponseDto response = fundService.processDeposit(depositRequest);

        assertEquals(new BigDecimal(100), response.getBonusBalance());
        assertEquals(new BigDecimal(200), response.getCashBalance());
        assertEquals(1L, response.getPlayerId());
        assertEquals(1L, response.getTransactionId());
    }

    @Test
    public void givenValidDepositRequestWithAmountUnderThreshold_WhenProcessDeposit_ThenDepositCashOnly(){
        //given
        TransactionRequestDto depositRequest = TransactionRequestDto.builder()
                .amount(new BigDecimal(50))
                .playerId(1L)
                .transactionId(1L).build();

        Wallet wallet = Wallet.builder().bonusBalance(new BigDecimal(0))
                .cashBalance(new BigDecimal(0)).build();

        Player player = Player.builder().id(1L) .username("testuser").wallet(wallet).build();

        //when
        when(transactionService.getAndCheckTransaction(depositRequest, TransactionType.DEPOSIT))
                .thenReturn(Optional.empty());

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        when(transactionMapper.transactionModelToTransactionResponseDto(any()))
                .thenCallRealMethod();

        //then
        TransactionResponseDto response = fundService.processDeposit(depositRequest);

        assertEquals(BigDecimal.ZERO, response.getBonusBalance());
        assertEquals(new BigDecimal(50), response.getCashBalance());
        assertEquals(1L, response.getPlayerId());
        assertEquals(1L, response.getTransactionId());
    }

    @Test
    public void givenValidWithdrawalRequest_WhenProcessWithdrawal_ThenWithdrawFunds(){
        //given
        TransactionRequestDto withdrawalRequest = TransactionRequestDto.builder()
                .amount(new BigDecimal(200))
                .playerId(1L)
                .transactionId(1L).build();

        Wallet wallet = Wallet.builder().bonusBalance(new BigDecimal(300))
                .cashBalance(new BigDecimal(300)).build();

        Player player = Player.builder().id(1L) .username("testuser").wallet(wallet).build();

        //when
        when(transactionService.getAndCheckTransaction(withdrawalRequest, TransactionType.WITHDRAWAL))
                .thenReturn(Optional.empty());

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        when(transactionMapper.transactionModelToTransactionResponseDto(any()))
                .thenCallRealMethod();

        //then
        TransactionResponseDto response = fundService.processWithdrawal(withdrawalRequest);

        assertEquals(new BigDecimal(300), response.getBonusBalance());
        assertEquals(new BigDecimal(100), response.getCashBalance());
        assertEquals(1L, response.getPlayerId());
        assertEquals(1L, response.getTransactionId());
    }

    @Test
    public void givenValidWithdrawalRequest_WhenProcessWithdrawalAndFundsLow_ThenThrowException(){
        //given
        TransactionRequestDto withdrawalRequest = TransactionRequestDto.builder()
                .amount(new BigDecimal(500))
                .playerId(1L)
                .transactionId(1L).build();

        Wallet wallet = Wallet.builder().bonusBalance(new BigDecimal(300))
                .cashBalance(new BigDecimal(300)).build();

        Player player = Player.builder().id(1L) .username("testuser").wallet(wallet).build();

        //when
        when(transactionService.getAndCheckTransaction(withdrawalRequest, TransactionType.WITHDRAWAL))
                .thenReturn(Optional.empty());

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        when(transactionMapper.transactionModelToTransactionResponseDto(any()))
                .thenCallRealMethod();

        //then
        TransactionResponseDto response = null;
        assertThrows(InsufficientFundsException.class, () ->
                fundService.processWithdrawal(withdrawalRequest));

        assertEquals(new BigDecimal(300), wallet.getBonusBalance());
        assertEquals(new BigDecimal(300), wallet.getCashBalance());
    }
}
