package com.abnamro.report.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Profile({"test"})
public class TradingReportControllerUnitTest {

    @Autowired
    protected WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private TradingReportService tradingReportServiceMock;

    @Autowired
    private TradingReportController tradingReportController;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.tradingReportController).build();// Standalone context
    }

    @Test
    public void shouldReturnTransactionWithAllThreeKeysWithServiceMocked() throws Exception {
        //Arrange
        Transaction transaction = Transaction.buildTransaction("CL-4321-0002-0001,SGX-FU-NK-20100910", "5");
        List<Transaction> transactions = Arrays.asList(transaction);
        Mockito.when(tradingReportServiceMock.getReport()).thenReturn(transactions);
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setClientInformation("CL-4321-0002-0001");
        expectedTransaction.setProductInformation("SGX-FU-NK-20100910");
        expectedTransaction.setTotalTransactionAmount("5");

        //Act
        List<Transaction> result = tradingReportController.getReport();

        //Assert
        assertThat(result.get(0).getClientInformation(), is(expectedTransaction.getClientInformation()));
        assertThat(result.get(0).getProductInformation(), is(expectedTransaction.getProductInformation()));
        assertThat(result.get(0).getTotalTransactionAmount(), is(expectedTransaction.getTotalTransactionAmount()));
    }
}
