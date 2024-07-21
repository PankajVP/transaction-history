package org.moneyforward.transactionhistory.util;

import org.moneyforward.transactionhistory.model.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtil {
    public static List<Transaction> parseCSV(String csvFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
        return lines.stream().skip(1).map(line -> {
            String[] parts = line.split(",");
            return new Transaction(parts[0], new BigDecimal(parts[1]), parts[2]);
        }).collect(Collectors.toList());
    }
}
