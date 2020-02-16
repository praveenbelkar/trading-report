package com.abnamro.report.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TransactionUtil {

    public String createClientInformation(String tradingRecord) {
        String clientType = tradingRecord.substring(3, 7).trim();
        String clientNumber = tradingRecord.substring(7, 11).trim();
        String accountNumber = tradingRecord.substring(11, 15).trim();
        String subAccountNumber = tradingRecord.substring(15, 19).trim();
        String clientInformation = String.join("-", Arrays.asList(clientType.trim(), clientNumber.trim(), accountNumber, subAccountNumber));
        return clientInformation;
    }

    public String createProductInformation(String tradingRecord) {
        String exchangeCode = tradingRecord.substring(27,31).trim();
        String productGroupCode = tradingRecord.substring(25,27).trim();
        String symbol = tradingRecord.substring(31, 37).trim();
        String expirationDate = tradingRecord.substring(37, 45).trim();
        String productInformation = String.join("-", Arrays.asList(exchangeCode.trim(), productGroupCode.trim(), symbol, expirationDate));
        return productInformation;
    }

    public String createTotalTransaction(String tradingRecord) {
        Long quantityLong = Long.parseLong(tradingRecord.substring(52,62).trim());
        Long quantityShort = Long.parseLong(tradingRecord.substring(63,73).trim());
        Long totalTransaction =  quantityLong - quantityShort;
        return totalTransaction.toString();
    }

    public String createKey(String tradingRecord) {
        StringBuilder sb = new StringBuilder();
        return sb.append(createClientInformation(tradingRecord)).append(",").append(createProductInformation(tradingRecord)).toString();
    }

}
