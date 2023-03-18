package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.DepositRequestDto;
import com.betting.javawalletsystem.dto.DepositResponseDto;
import com.betting.javawalletsystem.service.AuthorisationServiceImpl;
import com.betting.javawalletsystem.service.FundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FundsController {
    private final FundsService fundsService;

    private final AuthorisationServiceImpl authorisationService;

    public FundsController(@Autowired FundsService fundsService,
                           @Autowired AuthorisationServiceImpl authorisationService){
        this.fundsService = fundsService;
        this.authorisationService = authorisationService;
    }

    @PostMapping(value = "/deposit", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public DepositResponseDto depositFunds(@Valid @RequestBody DepositRequestDto deposit) {
        authorisationService.ensurePlayerAuthorised(deposit.getPlayerId());

        DepositResponseDto response = fundsService.processDeposit(deposit);

        return response;
    }

    @PostMapping(value ="/withdraw", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity withdrawFunds() {

        return null;
    }
}
