package org.moneyforward.transactionhistory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.moneyforward.transactionhistory.model.MonthlyStatement;
import org.moneyforward.transactionhistory.model.Transaction;
import org.moneyforward.transactionhistory.output.FileOutputStrategy;
import org.moneyforward.transactionhistory.output.OutputStrategy;
import org.moneyforward.transactionhistory.output.StandardOutputStrategy;

import java.math.BigDecimal;
import java.util.List;

import static org.moneyforward.transactionhistory.service.TransactionService.filterTransactions;
import static org.moneyforward.transactionhistory.util.CSVUtil.parseCSV;

@Slf4j
public class App {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            log.error("Please pass the correct number of command line arguments!");
            log.error("Usage: java FinancialHistory <YYYYMM> <CSV_FILE_PATH> [OUTPUT_FILE_PATH]");
            System.exit(1);
        }

        String period = args[0];
        String csvFilePath = args[1];
        String outputFilePath = args.length == 3 ? args[2] : null;

        OutputStrategy outputStrategy;
        if (outputFilePath != null) {
            outputStrategy = new FileOutputStrategy(outputFilePath);
        } else {
            outputStrategy = new StandardOutputStrategy();
        }

        try {
            List<Transaction> transactions = parseCSV(csvFilePath);
            List<Transaction> filteredTransactions = filterTransactions(transactions, period);

            BigDecimal totalIncome = getTotalIncome(filteredTransactions);

            BigDecimal totalExpenditure = getTotalExpenditure(filteredTransactions);

            MonthlyStatement statement = new MonthlyStatement(period, totalIncome, totalExpenditure, filteredTransactions);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(statement);

            outputStrategy.output(jsonOutput);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private static BigDecimal getTotalIncome(List<Transaction> filteredTransactions) {
        return filteredTransactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getTotalExpenditure(List<Transaction> filteredTransactions) {
        BigDecimal totalExpenditure = filteredTransactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalExpenditure;
    }
}
