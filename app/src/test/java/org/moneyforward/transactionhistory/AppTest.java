package org.moneyforward.transactionhistory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.moneyforward.transactionhistory.model.MonthlyStatement;
import org.moneyforward.transactionhistory.model.Transaction;
import org.moneyforward.transactionhistory.output.FileOutputStrategy;
import org.moneyforward.transactionhistory.output.OutputStrategy;
import org.moneyforward.transactionhistory.output.StandardOutputStrategy;
import org.moneyforward.transactionhistory.service.TransactionService;
import org.moneyforward.transactionhistory.util.CSVUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppTest {
    private static List<Transaction> transactions;
    private CSVUtil csvUtil;
    private TransactionService transactionService;
    private OutputStrategy outputStrategy;
    private App app;


    @BeforeEach
    void setUp() throws IOException {
        csvUtil = mock(CSVUtil.class);
        transactionService = mock(TransactionService.class);
        outputStrategy = mock(OutputStrategy.class);
        app = new App(csvUtil, transactionService);
        transactions = Arrays.asList(
                new Transaction("2022/01/01", BigDecimal.valueOf(100), "Salary"),
                new Transaction("2022/01/15", BigDecimal.valueOf(-50), "Groceries"),
                new Transaction("2022/01/20", BigDecimal.valueOf(200), "Freelance Work"),
                new Transaction("2022/01/25", BigDecimal.valueOf(-100), "Rent"),
                new Transaction("2022/01/30", BigDecimal.valueOf(50), "Gift"),
                new Transaction("2022/02/01", BigDecimal.valueOf(300), "Bonus"),
                new Transaction("2022/02/15", BigDecimal.valueOf(-60), "Utilities")
        );
    }


    @Test
    @DisplayName("Simple Run Test")
    void testMain() throws Exception {
        String period = "202201";
        String csvFilePath = "transactions_test.csv";
        String[] args = new String[]{period, csvFilePath};
        app.main(args);
    }

    @Test
    @DisplayName("Test FileOutputStrategy")
    void testMainFileOutput() throws Exception {
        String period = "202201";
        String csvFilePath = "transactions_test.csv";
        String outputFilePath = "output.json";
        String[] args = new String[]{period, csvFilePath, outputFilePath};

        app.main(args);
        // Verify the file output
        File file = new File(outputFilePath);
        assertTrue(file.exists(), "Output file should exist");

        // Clean up the output file
        file.delete();
    }

    @Test
    @DisplayName("Simple Run Test for run() method")
    void testRun() throws Exception {
        String period = "202201";
        String csvFilePath = "transactions_test.csv";
        app.run(period, csvFilePath, outputStrategy);

    }

    @Test
    @DisplayName("Simple Exception test")
    void testException() throws Exception {
        String period = "202201";
        String csvFilePath = "transactions_test.csv";
        String outputFilePath = "output.json";
        String[] args = new String[]{period, csvFilePath, outputFilePath};

        when(csvUtil.parseCSV(anyString())).thenThrow(new IOException("ERROR!"));

        app.main(args);
        //Once there is an error in CSV parsing, output strategy code should not be executed
        verify(outputStrategy, times(0)).output(anyString());
    }

    @Test
    @DisplayName("Simple Exception For CLI args test")
    void testExceptionArgs() throws Exception {
        String period = "202201";
        String[] args = new String[]{period};
        assertThrows(IllegalArgumentException.class,()->app.main(args));
    }


    @Test
    @DisplayName("Test Total Income Calculation")
    void testGetTotalIncome() {
        BigDecimal totalIncome = app.getTotalIncome(getFilteredTransaction());
        assertEquals(new BigDecimal("350"), totalIncome, "Total income for January 2022 should be 350.00");
    }

    @Test
    @DisplayName("Test Total Expenditure Calculation")
    void testGetTotalExpenditure() {

        BigDecimal totalExpenditure = app.getTotalExpenditure(getFilteredTransaction());
        assertEquals(new BigDecimal("-150"), totalExpenditure, "Total expenditure for January 2022 should be -150.00");
    }


    @Test
    @DisplayName("Test Total Income and Expenditure Calculation with No Transactions")
    void testEmptyTransactions() {
        List<Transaction> emptyTransactions = Collections.emptyList();
        BigDecimal totalIncome = app.getTotalIncome(emptyTransactions);
        BigDecimal totalExpenditure = app.getTotalExpenditure(emptyTransactions);
        assertEquals(BigDecimal.ZERO, totalIncome, "Total income with no transactions should be 0.00");
        assertEquals(BigDecimal.ZERO, totalExpenditure, "Total expenditure with no transactions should be 0.00");
    }
    @Test
    @DisplayName("Test Output to Console")
    void testOutputToConsole() throws Exception {
        OutputStrategy outputStrategy = new StandardOutputStrategy();
        MonthlyStatement statement = new MonthlyStatement("202201", BigDecimal.valueOf(350), BigDecimal.valueOf(-150.00), transactions);
        outputStrategy.output(statement.toString());
    }

    @Test
    @DisplayName("Test Output to File")
    void testOutputToFile() throws Exception {
        String filePath = "output.json";
        OutputStrategy outputStrategy = new FileOutputStrategy(filePath);
        MonthlyStatement statement = new MonthlyStatement("202201", BigDecimal.valueOf(350), BigDecimal.valueOf(-150.00), transactions);
        outputStrategy.output(statement.toString());

        // Verify the file output
        File file = new File(filePath);
        assertTrue(file.exists(), "Output file should exist");

        // Clean up the output file
        file.delete();
    }

    @Test
    @DisplayName("Test FileOutputStrategy")
    void testFileOutputStrategy() throws Exception {
        String filePath = "output.json";
        OutputStrategy outputStrategy = new FileOutputStrategy(filePath);
        MonthlyStatement statement = new MonthlyStatement("202201", BigDecimal.valueOf(350), BigDecimal.valueOf(-150.00), transactions);
        outputStrategy.output(statement.toString());

        // Verify the file output
        File file = new File(filePath);
        assertTrue(file.exists(), "Output file should exist");

        // Clean up the output file
        file.delete();
    }

    public List<Transaction> getFilteredTransaction() {
        return new TransactionService().filterTransactions(transactions, "202201");
    }
}
