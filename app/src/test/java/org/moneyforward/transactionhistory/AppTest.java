package org.moneyforward.transactionhistory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.moneyforward.transactionhistory.model.Transaction;
import org.moneyforward.transactionhistory.service.TransactionService;
import org.moneyforward.transactionhistory.util.CSVUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {
    private static List<Transaction> transactions;
    private App app;

    @BeforeAll
    public static void setUp() throws IOException {
        transactions = CSVUtil.parseCSV("transactions_test.csv");

    }

    @Test
    @DisplayName("Test Total Income Calculation")
    void testGetTotalIncome() {
        List<Transaction> januaryTransactions = TransactionService.filterTransactions(transactions, "202201");
        BigDecimal totalIncome = App.getTotalIncome(januaryTransactions);
        assertEquals(new BigDecimal("350.00"), totalIncome, "Total income for January 2022 should be 350.00");
    }

    @Test
    @DisplayName("Test Total Expenditure Calculation")
    void testGetTotalExpenditure() {
        List<Transaction> januaryTransactions = TransactionService.filterTransactions(transactions, "202201");
        BigDecimal totalExpenditure = App.getTotalExpenditure(januaryTransactions);
        assertEquals(new BigDecimal("-150.00"), totalExpenditure, "Total expenditure for January 2022 should be -150.00");
    }

    @Test
    @DisplayName("Test Filtering Transactions for January 2022")
    void testFilterTransactions() {
        List<Transaction> januaryTransactions = TransactionService.filterTransactions(transactions, "202201");
        assertEquals(5, januaryTransactions.size(), "Number of transactions for January 2022 should be 5");
    }

    @Test
    @DisplayName("Test Filtering Transactions with No Matches")
    void testFilterTransactionsNoMatch() {
        List<Transaction> marchTransactions = TransactionService.filterTransactions(transactions, "202203");
        assertEquals(0, marchTransactions.size(), "Number of transactions for March 2022 should be 0");
    }

    @Test
    @DisplayName("Test Total Income and Expenditure Calculation with No Transactions")
    void testEmptyTransactions() {
        List<Transaction> emptyTransactions = Collections.emptyList();
        BigDecimal totalIncome = App.getTotalIncome(emptyTransactions);
        BigDecimal totalExpenditure = App.getTotalExpenditure(emptyTransactions);
        assertEquals(BigDecimal.ZERO, totalIncome, "Total income with no transactions should be 0.00");
        assertEquals(BigDecimal.ZERO, totalExpenditure, "Total expenditure with no transactions should be 0.00");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/transactions_test.csv", numLinesToSkip = 1)
    @DisplayName("Test Parsing CSV File")
    void testParseCSV(String date, String amount, String content) throws IOException {
        Transaction expected = new Transaction(date, new BigDecimal(amount), content);
        List<Transaction> parsedTransactions = CSVUtil.parseCSV("transactions_test.csv");
        assertTrue(parsedTransactions.contains(expected), "Parsed transactions should contain the expected transaction");
    }

    @ParameterizedTest
    @MethodSource("providePeriodsAndExpectedSizes")
    @DisplayName("Test Filtering Transactions with Different Periods")
    void testFilterTransactionsWithDifferentPeriods(String period, int expectedSize) {
        List<Transaction> filteredTransactions = TransactionService.filterTransactions(transactions, period);
        assertEquals(expectedSize, filteredTransactions.size(),
                String.format("Number of transactions for period %s should be %d", period, expectedSize));
    }

    private static Stream<Arguments> providePeriodsAndExpectedSizes() {
        return Stream.of(
                Arguments.of("202201", 5),
                Arguments.of("202202", 2),
                Arguments.of("202203", 0)
        );
    }
}
