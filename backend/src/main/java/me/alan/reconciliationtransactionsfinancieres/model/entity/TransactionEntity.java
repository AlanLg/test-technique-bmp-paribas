package me.alan.reconciliationtransactionsfinancieres.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Data
public class TransactionEntity {

    private ObjectId id;
    private String primaryId;
    private String secondaryId;
    private TransactionEventEntity event;
    private String date;
    private Integer stepRank;
    private Integer eventRank;
    private List<TransactionErrors> transactionErrors;

    public TransactionEntity(TransactionEntity other) {
        this.id = other.id;
        this.primaryId = other.primaryId;
        this.secondaryId = other.secondaryId;
        this.event = other.event;
        this.date = other.date;
        this.stepRank = other.stepRank;
        this.eventRank = other.eventRank;
        this.transactionErrors = new ArrayList<>(other.transactionErrors);
    }
}
