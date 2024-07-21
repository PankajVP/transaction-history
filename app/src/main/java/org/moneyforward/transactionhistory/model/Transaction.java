package org.moneyforward.transactionhistory.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transaction {
    private String date;
    private BigDecimal amount;
    private String content;
}