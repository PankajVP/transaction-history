package org.moneyforward.transactionhistory.output;

public interface OutputStrategy {
    void output(String jsonOutput) throws Exception;
}