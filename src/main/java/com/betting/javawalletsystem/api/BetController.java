package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.BetUpdateRequestDto;
import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class BetController {
    private final BetService betService;

    public BetController(@Autowired BetService betService){
        this.betService = betService;
    }

    @PostMapping("/bet")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDto placeBet(@Valid @RequestBody TransactionRequestDto betRequest)
    {
        return betService.processBet(betRequest);
    }

    @PostMapping("/admin/bet/win")
    public TransactionResponseDto winBet(@Valid @RequestBody BetUpdateRequestDto winRequest) {
        return betService.updateBetStatus(winRequest);
    }
}
