package org.moneyforward.transactionhistory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.moneyforward.transactionhistory.model.MonthlyStatement;
import org.moneyforward.transactionhistory.model.Transaction;
import org.moneyforward.transactionhistory.output.FileOutputStrategy;
import org.moneyforward.transactionhistory.output.OutputStrategy;
import org.moneyforward.transactionhistory.output.StandardOutputStrategy;
import org.moneyforward.transactionhistory.service.TransactionService;
import org.moneyforward.transactionhistory.util.CSVUtil;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class App {
    private final CSVUtil csvUtil;
    private final TransactionService transactionService;

    public App(CSVUtil csvUtil, TransactionService transactionService) {
        this.csvUtil = csvUtil;
        this.transactionService = transactionService;
    }
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Please pass the correct number of command line arguments!\n Usage: java FinancialHistory <YYYYMM> <CSV_FILE_PATH> [OUTPUT_FILE_PATH]");
            throw new IllegalArgumentException("Please pass the correct number of command line arguments!");
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
        CSVUtil csvUtil = new CSVUtil();
        TransactionService transactionService = new TransactionService();
        App app = new App(csvUtil, transactionService);

        app.run(period, csvFilePath, outputStrategy);
    }

    public void run(String period, String csvFilePath, OutputStrategy outputStrategy) {
        try {
            List<Transaction> transactions = csvUtil.parseCSV(csvFilePath);
            List<Transaction> filteredTransactions = transactionService.filterTransactions(transactions, period);

            BigDecimal totalIncome = getTotalIncome(filteredTransactions);

            BigDecimal totalExpenditure = getTotalExpenditure(filteredTransactions);

            MonthlyStatement statement = new MonthlyStatement(period, totalIncome, totalExpenditure, filteredTransactions);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(statement);

            outputStrategy.output(jsonOutput);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    static BigDecimal getTotalIncome(List<Transaction> filteredTransactions) {
        return filteredTransactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static BigDecimal getTotalExpenditure(List<Transaction> filteredTransactions) {
        return filteredTransactions.stream()
                .map(Transaction::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
