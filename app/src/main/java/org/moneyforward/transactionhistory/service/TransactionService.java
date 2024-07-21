package org.moneyforward.transactionhistory.service;

import org.moneyforward.transactionhistory.model.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {
    public static List<Transaction> filterTransactions(List<Transaction> transactions, String period) {
        return transactions.stream()
                .filter(t -> t.getDate().startsWith(period.substring(0, 4) + "/"
                        + period.substring(4)))
                .sorted(Comparator.comparing((Transaction t) ->
                        LocalDate.parse(t.getDate(),
                                DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                        .reversed())
                .collect(Collectors.toList());
    }
}
