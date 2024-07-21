package org.moneyforward.transactionhistory.output;

import java.io.FileWriter;
import java.io.IOException;

public class FileOutputStrategy implements OutputStrategy {
    private final String filePath;

    public FileOutputStrategy(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void output(String jsonOutput) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonOutput);
            System.out.println("Output written to: " + filePath);
        }
    }
}