package com.betting.javawalletsystem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/transactions")
public class TransactionsController {
    @GetMapping
    public ResponseEntity listTransactions() {
        return null;
    }


}
