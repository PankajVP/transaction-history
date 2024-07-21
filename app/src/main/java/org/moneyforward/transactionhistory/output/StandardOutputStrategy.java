package org.moneyforward.transactionhistory.output;

public class StandardOutputStrategy implements OutputStrategy {
    @Override
    public void output(String jsonOutput) {
        System.out.println(jsonOutput);
    }
}