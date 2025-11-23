package me.alan.reconciliationtransactionsfinancieres.controller;

import me.alan.reconciliationtransactionsfinancieres.model.response.TransactionResponse;
import me.alan.reconciliationtransactionsfinancieres.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionChainController {

    private final TransactionService transactionService;

    public TransactionChainController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/chain/primaryId/{primaryId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionChainByPrimaryId(@PathVariable String primaryId) {
        return ResponseEntity.ok(transactionService.getTransactionChain(primaryId));
    }

    @GetMapping("/errors")
    public ResponseEntity<List<TransactionResponse>> getTransactionsWithErrors() {
        return ResponseEntity.ok(transactionService.getTransactionsWithErrors());
    }
}
