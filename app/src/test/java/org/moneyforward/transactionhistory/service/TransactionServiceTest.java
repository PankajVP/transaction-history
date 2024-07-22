package org.moneyforward.transactionhistory.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.moneyforward.transactionhistory.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionServiceTest {

    private List<Transaction> transactions;

    @BeforeEach
    public void setUp() {
        transactions = Arrays.asList(
                new Transaction("2023/07/01", new BigDecimal("100.50"), "Groceries"),
                new Transaction("2023/07/02", new BigDecimal("200.00"), "Rent"),
                new Transaction("2022/07/01", new BigDecimal("150.75"), "Utilities"),
                new Transaction("2023/06/30", new BigDecimal("300.25"), "Salary")
        );
    }

    @Test
    public void testFilterTransactions_yearMonth() {
        List<Transaction> filtered = TransactionService.filterTransactions(transactions, "202307");

        assertEquals(2, filtered.size());
        assertEquals("2023/07/02", filtered.get(0).getDate());
        assertEquals("2023/07/01", filtered.get(1).getDate());
    }

    @Test
    public void testFilterTransactions_year() {
        List<Transaction> filtered = TransactionService.filterTransactions(transactions, "2023");

        assertEquals(3, filtered.size());
        assertEquals("2023/07/02", filtered.get(0).getDate());
        assertEquals("2023/07/01", filtered.get(1).getDate());
        assertEquals("2023/06/30", filtered.get(2).getDate());
    }

    @Test
    public void testFilterTransactions_empty() {
        List<Transaction> filtered = TransactionService.filterTransactions(transactions, "202201");

        assertEquals(0, filtered.size());
    }

    @Test
    public void testFilterTransactions_invalidPeriod() {
        List<Transaction> filtered = TransactionService.filterTransactions(transactions, "invalid");

        assertEquals(0, filtered.size());
    }
}

