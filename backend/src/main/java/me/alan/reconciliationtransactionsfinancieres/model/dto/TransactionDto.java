package me.alan.reconciliationtransactionsfinancieres.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionDto(
        @JsonProperty("primary_id")
        String primaryId,
        @JsonProperty("secondary_id")
        String secondaryId,
        @JsonProperty("event")
        TransactionEventDto event,
        @JsonProperty("date")
        String date
    ) {
}
