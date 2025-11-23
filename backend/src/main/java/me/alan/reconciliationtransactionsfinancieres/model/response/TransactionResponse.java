package me.alan.reconciliationtransactionsfinancieres.model.response;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class TransactionResponse {
    private String primaryId;
    private String secondaryId;
    private String eventType;
    private String date;
    private Integer stepRank;
    private Integer eventRank;
    private List<String> transactionErrors;
}
