package me.alan.reconciliationtransactionsfinancieres.service;

import me.alan.reconciliationtransactionsfinancieres.model.dto.ReferenceStepDto;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionErrors;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEventEntity;
import me.alan.reconciliationtransactionsfinancieres.model.response.TransactionResponse;
import me.alan.reconciliationtransactionsfinancieres.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Test
    void itShouldReturnMappedResponsesForTransactionsWithErrors() {
        // Arrange
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        StepReferenceService stepReferenceService = Mockito.mock(StepReferenceService.class);

        TransactionEntity e1 = new TransactionEntity();
        e1.setPrimaryId("P1");
        e1.setSecondaryId("S1");
        e1.setEvent(new TransactionEventEntity("CREATED"));
        e1.setDate("2024-01-01");
        e1.setStepRank(1);
        e1.setEventRank(1);
        e1.setTransactionErrors(List.of(TransactionErrors.INVALID_DATE_TIME));

        when(repository.findTransactionEntitiesByTransactionErrorsNotEmpty())
                .thenReturn(List.of(e1));

        TransactionService service = new TransactionService(repository, stepReferenceService);

        // Act
        List<TransactionResponse> responses = service.getTransactionsWithErrors();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        TransactionResponse r = responses.get(0);
        assertEquals("P1", r.getPrimaryId());
        assertEquals("CREATED", r.getEventType());
        assertEquals(1, r.getStepRank());
        assertEquals(1, r.getEventRank());
        assertEquals(List.of("Invalid date"), r.getTransactionErrors());
    }

    @Test
    void itShouldReturnSingleElementChainWhenNoLinks() {
        // Arrange
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        StepReferenceService stepReferenceService = Mockito.mock(StepReferenceService.class);

        LinkedList<ReferenceStepDto> refs = new LinkedList<>();
        ReferenceStepDto first = new ReferenceStepDto();
        first.setStepRank(1);
        first.setEventRank(1);
        first.setEventType("CREATED");
        refs.add(first);
        ReferenceStepDto last = new ReferenceStepDto();
        last.setStepRank(2);
        last.setEventRank(2);
        last.setEventType("UPDATED");
        refs.add(last);

        when(stepReferenceService.getLoadReferenceSteps()).thenReturn(refs);

        TransactionEntity base = new TransactionEntity();
        base.setPrimaryId("P_BASE");
        base.setSecondaryId(null);
        base.setEvent(new TransactionEventEntity("CREATED"));
        base.setDate("2024-01-01");
        base.setStepRank(1);
        base.setEventRank(1);
        base.setTransactionErrors(List.of());

        when(repository.findTransactionEntityByPrimaryId("P_BASE")).thenReturn(base);
        when(repository.findTransactionEntityBySecondaryId(anyString())).thenReturn(null);
        when(repository.findTransactionEntityByPrimaryId(anyString())).thenReturn(base);

        TransactionService service = new TransactionService(repository, stepReferenceService);

        // Act
        List<TransactionResponse> chain = service.getTransactionChain("P_BASE");

        // Assert
        assertNotNull(chain);
        assertEquals(1, chain.size());
        assertEquals("P_BASE", chain.get(0).getPrimaryId());
        assertEquals("CREATED", chain.get(0).getEventType());
    }
}
