package org.moneyforward.transactionhistory.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.moneyforward.transactionhistory.model.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CSVUtil {
    public static List<Transaction> parseCSV(String csvFilePath) throws IOException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(csvFilePath));
        } catch (NoSuchFileException e) {
            try {
                lines = Files.readAllLines(Paths.get(CSVUtil.class.getResource("/" + csvFilePath).toURI()));
            } catch (Exception innerException) {
                throw new IOException("File not found in both the specified path and resources directory: " + csvFilePath, innerException);
            }
        }

        return lines.stream().skip(1).map(line -> {
            String[] parts = line.split(",");
            return new Transaction(parts[0], new BigDecimal(parts[1]), parts[2]);
        }).collect(Collectors.toList());
    }
}
