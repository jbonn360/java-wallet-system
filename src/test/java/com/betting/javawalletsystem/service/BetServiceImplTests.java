package com.betting.javawalletsystem.service;

import com.betting.javawalletsystem.dto.BetUpdateRequestDto;
import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.mappers.TransactionMapperImpl;
import com.betting.javawalletsystem.model.*;
import com.betting.javawalletsystem.repository.BetRepository;
import com.betting.javawalletsystem.repository.PlayerRepository;
import com.betting.javawalletsystem.repository.TransactionRepository;
import com.betting.javawalletsystem.repository.WalletRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BetServiceImplTests {
    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final WalletRepository walletRepository = mock(WalletRepository.class);
    private final BetRepository betRepository = mock(BetRepository.class);
    private final TransactionMapperImpl transactionMapper = mock(TransactionMapperImpl.class);
    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    private final TransactionService transactionService = new TransactionServiceImpl(
            transactionRepository, transactionMapper
    );

    private final BetServiceImpl betService = new BetServiceImpl(
            playerRepository, walletRepository, betRepository, spy(transactionService), transactionMapper
    );

    @Test
    public void givenValidBetRequest_WhenProcessBet_ThenBetIsPlaced(){
        //given
        TransactionRequestDto request = TransactionRequestDto.builder()
                .transactionId(1L)
                .playerId(1L)
                .amount(new BigDecimal(150)).build();

        Wallet wallet = Wallet.builder().bonusBalance(new BigDecimal(100))
                .cashBalance(new BigDecimal(100)).build();

        Player player = Player.builder().id(1L) .username("testuser").wallet(wallet).build();

        //when
        when(transactionService.getAndCheckTransaction(request, TransactionType.BET_PLACEMENT))
                .thenReturn(Optional.empty());
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(betRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        when(transactionMapper.transactionModelToTransactionResponseDto(any()))
                .thenCallRealMethod();

        //then
        TransactionResponseDto response = betService.processBet(request);

        assertEquals(1L, response.getPlayerId());
        assertEquals(1L, response.getTransactionId());
        assertEquals(BigDecimal.ZERO, response.getCashBalance());
        assertEquals(new BigDecimal(50), response.getBonusBalance());
    }

    @Test
    public void givenValidBetUpdateRequest_WhenUpdateBetStatus_ThenBetIsUpdated(){
        //given
        BetUpdateRequestDto request = new BetUpdateRequestDto(2L, 1L, new BigDecimal(600),
                1L);

        Wallet wallet = Wallet.builder().bonusBalance(new BigDecimal(100))
                .cashBalance(new BigDecimal(100)).build();

        Player player = Player.builder().id(1L).username("testuser").wallet(wallet).build();

        Bet bet = Bet.builder().betStatus(BetStatus.PLACED)
                .bonusAmount(new BigDecimal(200))
                .cashAmount(new BigDecimal(100))
                .transactionId(1L)
                .player(player).build();

        //when
        when(transactionService.getAndCheckTransaction(request, TransactionType.BET_PLACEMENT))
                .thenReturn(Optional.empty());
        when(betRepository.findByTransactionId(1L)).thenReturn(Optional.of(bet));
        when(betRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        when(transactionMapper.transactionModelToTransactionResponseDto(any()))
                .thenCallRealMethod();

        //then
        TransactionResponseDto response = betService.updateBetStatus(request);

        BigDecimal expectedCash = new BigDecimal(300.00).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal expectedBonus = new BigDecimal(500.00).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        assertEquals(expectedCash, response.getCashBalance());
        assertEquals(expectedBonus, response.getBonusBalance());
        assertEquals(1L, response.getPlayerId());
        assertEquals(2L, response.getTransactionId());
    }
}
