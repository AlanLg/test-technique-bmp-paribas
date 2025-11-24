package me.alan.reconciliationtransactionsfinancieres.service;

import lombok.extern.slf4j.Slf4j;
import me.alan.reconciliationtransactionsfinancieres.mapper.TransactionMapper;
import me.alan.reconciliationtransactionsfinancieres.model.dto.ReferenceStepDto;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import me.alan.reconciliationtransactionsfinancieres.model.response.TransactionResponse;
import me.alan.reconciliationtransactionsfinancieres.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final StepReferenceService stepReferenceService;

    public TransactionService(TransactionRepository transactionRepository, StepReferenceService stepReferenceService) {
        this.transactionRepository = transactionRepository;
        this.stepReferenceService = stepReferenceService;
    }

    public List<TransactionResponse> getTransactionsWithErrors() {
        return TransactionMapper.INSTANCE.transactionEntityToTransactionResponse(
                transactionRepository.findTransactionEntitiesByTransactionErrorsNotEmpty()
        );
    }

    public List<TransactionResponse> getTransactionChain(String primaryId) {
        final TransactionEntity transaction = transactionRepository.findTransactionEntityByPrimaryId(primaryId);
        final ReferenceStepDto firstStep = stepReferenceService.getLoadReferenceSteps().getFirst();
        final ReferenceStepDto lastStep = stepReferenceService.getLoadReferenceSteps().getLast();

        final List<TransactionEntity> transactionsAboveSearch = constructChain(transaction, firstStep, (currentTransaction) -> {
            return transactionRepository.findTransactionEntityByPrimaryId(currentTransaction.getSecondaryId());
        });

        Collections.reverse(transactionsAboveSearch);

        transactionsAboveSearch.add(transaction);

        final List<TransactionEntity> transactionsBellowSearch = constructChain(transaction, lastStep, (currentTransaction) -> {
            return transactionRepository.findTransactionEntityBySecondaryId(currentTransaction.getPrimaryId());
        });

        final List<TransactionEntity> completeTransactionChain = Stream.concat(transactionsAboveSearch.stream(), transactionsBellowSearch.stream())
                .collect(Collectors.toList());

        return TransactionMapper.INSTANCE.transactionEntityToTransactionResponse(completeTransactionChain);
    }

    private List<TransactionEntity> constructChain(TransactionEntity transaction, ReferenceStepDto referenceStep, Function<TransactionEntity, TransactionEntity> findNextTransaction) {
        final List<TransactionEntity> transactionChain = new ArrayList<>();
        TransactionEntity currentTransaction = new TransactionEntity(transaction);
        while ((currentTransaction.getSecondaryId() != null) && (!currentTransaction.getEventRank().equals(referenceStep.getEventRank()) || !currentTransaction.getStepRank().equals(referenceStep.getStepRank()))) {
            currentTransaction = findNextTransaction.apply(currentTransaction);
            transactionChain.add(new TransactionEntity(currentTransaction));
        }
        return transactionChain;
    }
}
