package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.TransactionListDto;
import com.betting.javawalletsystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/transactions")
public class TransactionsController {
    private final TransactionService transactionService;

    public TransactionsController(@Autowired TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping(params = { "limit", "offset" }, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public TransactionListDto listTransactions(
            @RequestParam("limit") int limit, @RequestParam("offset") int offset)
    {
        TransactionListDto result = transactionService.getAllTransactions(limit, offset);

        return result;
    }


}
