package com.abnamro.report.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

    @JsonProperty("Client_Information")
    private String clientInformation;
    @JsonProperty("Product_Information")
    private String productInformation;
    @JsonProperty("Total_Transaction_Amount")
    private String totalTransactionAmount;

    public String getClientInformation() {
        return clientInformation;
    }

    public void setClientInformation(String clientInformation) {
        this.clientInformation = clientInformation;
    }

    public String getProductInformation() {
        return productInformation;
    }

    public void setProductInformation(String productInformation) {
        this.productInformation = productInformation;
    }

    public String getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(String totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public static Transaction buildTransaction(String key, String value) {
        Transaction transaction = new Transaction();
        String[] clientInfoArray = key.split(",");
        transaction.setClientInformation(clientInfoArray[0]);
        transaction.setProductInformation(clientInfoArray[1]);
        transaction.setTotalTransactionAmount(value);
        return transaction;
    }
}
