package me.alan.reconciliationtransactionsfinancieres.service;

import lombok.Getter;
import me.alan.reconciliationtransactionsfinancieres.model.dto.ReferenceStepDto;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionErrors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class StepReferenceService {

    @Getter
    private final LinkedList<ReferenceStepDto> loadReferenceSteps;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    public StepReferenceService(ObjectMapper objectMapper, MongoTemplate mongoTemplate) {
        this.objectMapper = objectMapper;
        this.mongoTemplate = mongoTemplate;
        try {
            this.loadReferenceSteps = loadReferenceSteps();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LinkedList<ReferenceStepDto> loadReferenceSteps() throws IOException {
        final ClassPathResource resource = new ClassPathResource("reference.json");
        final InputStream inputStream = resource.getInputStream();
        final String json = new String(FileCopyUtils.copyToByteArray(inputStream));

        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    public void markNullStepAndEventRanksWithTransactionError() {
        final Update update = new Update()
                .addToSet("transactionErrors", TransactionErrors.INCOMPLETE_TRANSACTION_CHAIN);

        final Query query = new Query(where("stepRank").is(-1));

        mongoTemplate.updateMulti(
                query,
                update,
                TransactionEntity.class
        );
    }

    public void updateStepRanksAndEventRanksForTransactions() {
        for (ReferenceStepDto referenceStep : loadReferenceSteps) {
            switch (referenceStep.getStepRank()) {
                case 1 -> updateStepRankOneTransactions(referenceStep);
                case 2 -> updateStepRankTwoTransactions(referenceStep);
            }
        }
    }

    public void updateStepRankOneTransactions(final ReferenceStepDto referenceStep) {
        final Update update = new Update()
                .set("stepRank", referenceStep.getStepRank())
                .set("eventRank", referenceStep.getEventRank());

        final Query query = new Query(where("event.eventType").is(referenceStep.getEventType()));

        mongoTemplate.updateMulti(
                query,
                update,
                TransactionEntity.class
        );
    }

    public void updateStepRankTwoTransactions(final ReferenceStepDto referenceStep) {
        final List<String> previousIds =  mongoTemplate.findDistinct(
                Query.query(Criteria.where("eventRank").ne(-1)),
                "primaryId",
                TransactionEntity.class,
                String.class
        );

        final Query query = new Query();
        query.addCriteria(Criteria.where("event.eventType").is(referenceStep.getEventType()));
        query.addCriteria(Criteria.where("eventRank").is(-1));
        query.addCriteria(Criteria.where("secondaryId").in(previousIds));

        final Update update = new Update()
                .set("stepRank", referenceStep.getStepRank())
                .set("eventRank", referenceStep.getEventRank());

        mongoTemplate.updateMulti(
                query,
                update,
                TransactionEntity.class
        );
    }
}
