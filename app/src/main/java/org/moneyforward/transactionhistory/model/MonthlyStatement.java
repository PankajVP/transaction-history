package org.moneyforward.transactionhistory.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class MonthlyStatement {
    private String period;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenditure;
    private List<Transaction> transactions;
}