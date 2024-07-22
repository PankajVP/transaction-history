package org.moneyforward.transactionhistory.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.moneyforward.transactionhistory.model.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVUtilTest {
    private CSVUtil csvUtil;

    @TempDir
    Path tempDir;

    Path csvFilePath;
    Path missingFilePath;
    Path resourceFilePath;

    @BeforeEach
    public void setUp() throws IOException {
        csvUtil=new CSVUtil();
        csvFilePath = tempDir.resolve("test.csv");
        Files.write(csvFilePath, List.of(
                "date,amount,content",
                "2023-07-01,100.50,Groceries",
                "2023-07-02,200.00,Rent"
        ));

        missingFilePath = tempDir.resolve("missing.csv");

        resourceFilePath = csvFilePath.toAbsolutePath();
    }

    @Test
    void testParseCSV_success() throws IOException {
        List<Transaction> transactions = csvUtil.parseCSV(csvFilePath.toString());

        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        Transaction transaction1 = transactions.get(0);
        assertEquals("2023-07-01", transaction1.getDate());
        assertEquals(new BigDecimal("100.50"), transaction1.getAmount());
        assertEquals("Groceries", transaction1.getContent());

        Transaction transaction2 = transactions.get(1);
        assertEquals("2023-07-02", transaction2.getDate());
        assertEquals(new BigDecimal("200.00"), transaction2.getAmount());
        assertEquals("Rent", transaction2.getContent());
    }

    @Test
    void testParseCSV_fileNotFound() {
        IOException exception = assertThrows(IOException.class, () -> {
            csvUtil.parseCSV(missingFilePath.toString());
        });

        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testParseCSV_fromResources() throws IOException {
        List<Transaction> transactions = csvUtil.parseCSV(resourceFilePath.toString());

        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        Transaction transaction1 = transactions.get(0);
        assertEquals("2023-07-01", transaction1.getDate());
        assertEquals(new BigDecimal("100.50"), transaction1.getAmount());
        assertEquals("Groceries", transaction1.getContent());

        Transaction transaction2 = transactions.get(1);
        assertEquals("2023-07-02", transaction2.getDate());
        assertEquals(new BigDecimal("200.00"), transaction2.getAmount());
        assertEquals("Rent", transaction2.getContent());
    }
}
