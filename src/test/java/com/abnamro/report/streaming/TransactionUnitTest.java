package com.abnamro.report.streaming;

import com.abnamro.report.api.Transaction;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TransactionUnitTest {

    @Test
    public void shouldSplitKeyOnComma() {
        //Act
        Transaction transaction = Transaction.buildTransaction("client,product", "10");

        //Assert
        assertThat(transaction, notNullValue());
        assertThat(transaction.getClientInformation(), is("client"));
        assertThat(transaction.getProductInformation(), is("product"));
        assertThat(transaction.getTotalTransactionAmount(), is("10"));
    }
}
