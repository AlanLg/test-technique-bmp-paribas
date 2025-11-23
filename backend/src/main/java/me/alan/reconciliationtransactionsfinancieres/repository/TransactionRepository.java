package me.alan.reconciliationtransactionsfinancieres.repository;

import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TransactionRepository extends MongoRepository<TransactionEntity, String> {

    TransactionEntity findTransactionEntityByPrimaryId(String primaryId);
    TransactionEntity findTransactionEntityBySecondaryId(String secondaryId);
    @Query("{ 'transactionErrors' : { $ne: [] } }")
    List<TransactionEntity> findTransactionEntitiesByTransactionErrorsNotEmpty();
}
