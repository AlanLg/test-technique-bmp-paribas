package me.alan.reconciliationtransactionsfinancieres.model.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class ReferenceStepDto {
    private String stepCode;
    private Integer stepRank;
    private Integer eventRank;
    private String eventType;
}
