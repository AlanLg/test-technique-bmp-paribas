package me.alan.reconciliationtransactionsfinancieres.mapper;

import me.alan.reconciliationtransactionsfinancieres.model.dto.TransactionDto;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionErrors;
import me.alan.reconciliationtransactionsfinancieres.model.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "primaryId", source = "primaryId")
    @Mapping(target = "secondaryId", source = "secondaryId")
    @Mapping(target = "event.eventType", source = "event.eventType")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "transactionErrors", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "stepRank", constant = "-1")
    @Mapping(target = "eventRank", constant = "-1")
    TransactionEntity transactionDtoToTransactionEntity(TransactionDto transactionDto);

    @Mapping(target = "primaryId", source = "primaryId")
    @Mapping(target = "secondaryId", source = "secondaryId")
    @Mapping(target = "eventType", source = "event.eventType")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "transactionErrors", source = "transactionErrors", qualifiedByName = "getTransactionErrorMessages")
    @Mapping(target = "stepRank", source = "stepRank")
    @Mapping(target = "eventRank", source = "eventRank")
    TransactionResponse transactionEntityToTransactionResponse(TransactionEntity transactionEntity);
    List<TransactionResponse> transactionEntityToTransactionResponse(List<TransactionEntity> transactionEntity);

    @Named("getTransactionErrorMessages")
    default List<String> getTransactionErrorMessages(List<TransactionErrors> transactionErrors) {
        return transactionErrors.stream().map(TransactionErrors::getMessage).collect(Collectors.toList());
    }
}
