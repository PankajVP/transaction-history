# Test Case Documentation

## TransactionServiceTest

| Test Method                          | Description                                                                                         | Expected Result                                                    |
|--------------------------------------|-----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| `testFilterTransactions_yearMonth()` | Tests the filtering of transactions for a specific year and month ("202307").                       | Returns 2 transactions dated "2023/07/02" and "2023/07/01".        |
| `testFilterTransactions_year()`      | Tests the filtering of transactions for a specific year ("2023").                                   | Returns 3 transactions dated "2023/07/02", "2023/07/01", "2023/06/30". |
| `testFilterTransactions_empty()`     | Tests the filtering of transactions for a period with no transactions ("202201").                   | Returns an empty list.                                             |
| `testFilterTransactions_invalidPeriod()` | Tests the filtering of transactions with an invalid period ("invalid").                            | Returns an empty list.                                             |

## CSVUtilTest

| Test Method                | Description                                                                             | Expected Result                                                                                         |
|----------------------------|-----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| `testParseCSV_success()`   | Tests parsing a valid CSV file.                                                          | Returns a list of transactions with the correct data.                                                   |
| `testParseCSV_fileNotFound()` | Tests parsing a CSV file that does not exist.                                           | Throws an IOException with a message containing "File not found".                                       |
| `testParseCSV_fromResources()` | Tests parsing a CSV file from resources.                                               | Returns a list of transactions with the correct data.                                                   |

## AppTest

| Test Method                          | Description                                                                                         | Expected Result                                                    |
|--------------------------------------|-----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| `testMain()`                         | Tests the main method of the App with valid arguments.                                              | Runs without exceptions.                                           |
| `testMainFileOutput()`               | Tests the main method of the App with file output arguments.                                        | Generates an output file "output.json".                            |
| `testRun()`                          | Tests the run method of the App with valid arguments.                                               | Runs without exceptions.                                           |
| `testException()`                    | Tests the main method of the App when an exception occurs during CSV parsing.                       | Verifies the output strategy is not executed.                      |
| `testExceptionArgs()`                | Tests the main method of the App with insufficient CLI arguments.                                   | Throws an IllegalArgumentException.                                |
| `testGetTotalIncome()`               | Tests the calculation of total income from filtered transactions.                                   | Returns the correct total income for January 2022 (350.00).        |
| `testGetTotalExpenditure()`          | Tests the calculation of total expenditure from filtered transactions.                              | Returns the correct total expenditure for January 2022 (-150.00).  |
| `testEmptyTransactions()`            | Tests the calculation of total income and expenditure with no transactions.                         | Returns 0.00 for both income and expenditure.                      |
| `testOutputToConsole()`              | Tests output to console using `StandardOutputStrategy`.                                             | Outputs the statement to the console.                              |
| `testOutputToFile()`                 | Tests output to file using `FileOutputStrategy`.                                                    | Generates an output file "output.json".                            |
| `testFileOutputStrategy()`           | Tests the `FileOutputStrategy` independently.                                                       | Generates an output file "output.json".                            |
