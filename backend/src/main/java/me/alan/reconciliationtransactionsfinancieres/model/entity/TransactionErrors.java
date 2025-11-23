package me.alan.reconciliationtransactionsfinancieres.model.entity;

import lombok.Getter;

public enum TransactionErrors {
    INVALID_DATE_TIME("Invalid date"),
    ;

    @Getter
    private final String message;

    TransactionErrors(String message) {
        this.message = message;
    }
}
