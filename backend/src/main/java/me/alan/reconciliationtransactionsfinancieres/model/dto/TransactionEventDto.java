package me.alan.reconciliationtransactionsfinancieres.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionEventDto(
        @JsonProperty("eventType")
        String eventType
) {
}
