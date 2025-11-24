package me.alan.reconciliationtransactionsfinancieres.model.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEntityTest {

    @Test
    void itShouldCreateDeepCopyForErrorsListWithCopyConstructor() {
        // Arrange
        TransactionEntity original = new TransactionEntity();
        original.setPrimaryId("P1");
        original.setSecondaryId("S1");
        original.setEvent(new TransactionEventEntity("CREATED"));
        original.setDate("2024-01-01");
        original.setStepRank(1);
        original.setEventRank(1);
        List<TransactionErrors> errors = new ArrayList<>();
        errors.add(TransactionErrors.INVALID_DATE_TIME);
        original.setTransactionErrors(errors);

        // Act
        TransactionEntity copy = new TransactionEntity(original);

        // Assert
        assertEquals(original.getPrimaryId(), copy.getPrimaryId());
        assertEquals(original.getSecondaryId(), copy.getSecondaryId());
        assertEquals(original.getEvent(), copy.getEvent());
        assertEquals(original.getDate(), copy.getDate());
        assertEquals(original.getStepRank(), copy.getStepRank());
        assertEquals(original.getEventRank(), copy.getEventRank());

        // Assert
        assertNotSame(original.getTransactionErrors(), copy.getTransactionErrors());
        assertEquals(1, copy.getTransactionErrors().size());

        // Assert
        copy.getTransactionErrors().add(TransactionErrors.INCOMPLETE_TRANSACTION_CHAIN);
        assertEquals(1, original.getTransactionErrors().size());
        assertEquals(2, copy.getTransactionErrors().size());
    }
}
