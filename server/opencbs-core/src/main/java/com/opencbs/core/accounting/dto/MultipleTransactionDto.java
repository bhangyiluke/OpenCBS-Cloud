package com.opencbs.core.accounting.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;

@Data
public class MultipleTransactionDto {
    @NotEmpty
    private List<MultipleTransactionAmountDto> amounts;
    private Long accountId;
    private Boolean accountWillBeDebit;
    private String description;
    private LocalDateTime dateTime;
}
