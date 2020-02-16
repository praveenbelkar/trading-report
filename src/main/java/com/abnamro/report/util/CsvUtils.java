package com.abnamro.report.util;

import com.abnamro.report.api.Transaction;

import java.io.PrintWriter;
import java.util.List;

public class CsvUtils {

    public static void downloadCsv(PrintWriter writer, List<Transaction> transactions) {
        writer.write("Client_Information, Product_Information, Total_Transaction_Amount \n");
        for (Transaction transaction : transactions) {
            writer.write(transaction.getClientInformation() + "," + transaction.getProductInformation() + "," + transaction.getTotalTransactionAmount() + "\n");
        }
    }
}
