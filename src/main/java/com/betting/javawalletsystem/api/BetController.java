package com.betting.javawalletsystem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/bet")
public class BetController {
    @PostMapping
    public ResponseEntity placeBet() {
        return null;
    }

    @PostMapping("/win")
    public ResponseEntity winBet() {
        return null;
    }
}
