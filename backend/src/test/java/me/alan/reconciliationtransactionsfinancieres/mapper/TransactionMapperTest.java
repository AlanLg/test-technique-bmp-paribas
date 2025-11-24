package me.alan.reconciliationtransactionsfinancieres.mapper;

import me.alan.reconciliationtransactionsfinancieres.model.dto.TransactionDto;
import me.alan.reconciliationtransactionsfinancieres.model.dto.TransactionEventDto;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionErrors;
import me.alan.reconciliationtransactionsfinancieres.model.response.TransactionResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    @Test
    void itShouldMapDtoToEntityWithDefaultValuesApplied() {
        // Arrange
        TransactionDto dto = new TransactionDto(
                "P1",
                "S1",
                new TransactionEventDto("CREATED"),
                "2024-01-01T00:00:00Z"
        );

        // Act
        TransactionEntity entity = TransactionMapper.INSTANCE.transactionDtoToTransactionEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("P1", entity.getPrimaryId());
        assertEquals("S1", entity.getSecondaryId());
        assertNotNull(entity.getEvent());
        assertEquals("CREATED", entity.getEvent().eventType());
        assertEquals("2024-01-01T00:00:00Z", entity.getDate());
        assertEquals(-1, entity.getStepRank());
        assertEquals(-1, entity.getEventRank());
        assertNotNull(entity.getTransactionErrors());
        assertTrue(entity.getTransactionErrors().isEmpty());
    }

    @Test
    void itShouldMapEntityToResponseAndMapErrorMessages() {
        // Arrange
        TransactionEntity entity = new TransactionEntity();
        entity.setPrimaryId("P2");
        entity.setSecondaryId("S2");
        entity.setEvent(new me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEventEntity("UPDATED"));
        entity.setDate("2024-02-02T00:00:00Z");
        entity.setStepRank(1);
        entity.setEventRank(2);
        entity.setTransactionErrors(List.of(
                TransactionErrors.INVALID_DATE_TIME,
                TransactionErrors.INCOMPLETE_TRANSACTION_CHAIN
        ));

        // Act
        TransactionResponse response = TransactionMapper.INSTANCE.transactionEntityToTransactionResponse(entity);

        // Assert
        assertNotNull(response);
        assertEquals("P2", response.getPrimaryId());
        assertEquals("S2", response.getSecondaryId());
        assertEquals("UPDATED", response.getEventType());
        assertEquals("2024-02-02T00:00:00Z", response.getDate());
        assertEquals(1, response.getStepRank());
        assertEquals(2, response.getEventRank());
        assertNotNull(response.getTransactionErrors());
        assertEquals(2, response.getTransactionErrors().size());
        assertTrue(response.getTransactionErrors().contains("Invalid date"));
        assertTrue(response.getTransactionErrors().contains("Incomplete transaction chain"));
    }
}
