package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.DepositRequestDto;
import com.betting.javawalletsystem.service.FundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/api/v1")
public class FundsController {

    private final FundsService fundsService;
    public FundsController(@Autowired FundsService fundsService){
        this.fundsService = fundsService;
    }

    @PostMapping("/deposit")
    public ResponseEntity depositFunds(@Valid @RequestBody DepositRequestDto deposit) {

        return null;
    }

    @PostMapping("/withdraw")
    public ResponseEntity withdrawFunds() {
        return null;
    }
}
