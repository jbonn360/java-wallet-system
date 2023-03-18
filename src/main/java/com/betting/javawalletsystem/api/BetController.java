package com.betting.javawalletsystem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BetController {
    @PostMapping("/bet")
    public ResponseEntity placeBet() {
        return null;
    }

    @PostMapping("/admin/bet/win")
    public ResponseEntity winBet() {
        return null;
    }
}
