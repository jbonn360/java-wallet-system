package com.betting.javawalletsystem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
public class WalletController {
    @PostMapping("/deposit")
    public ResponseEntity depositFunds() {
        return null;
    }

    @PostMapping("/withdraw")
    public ResponseEntity withdrawFunds() {
        return null;
    }
}
