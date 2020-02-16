package com.abnamro.report.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Profile({"test"})
public class TradingReportWebTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private TradingReportController tradingReportController;

    @MockBean
    private TradingReportService tradingReportServiceMock;

    private MockMvc mockMvc;
    private InputStream is;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.tradingReportController).build();// Standalone context
        this.is = this.getClass().getClassLoader().getResourceAsStream("transactionReport.csv");
    }

    @Test
    public void shouldReturnTransactionWithAllThreeKeys() throws Exception {
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

    @Test
    public void shouldReturnValidJsonForWebInvocation() throws Exception {
        //Arrange
        Transaction transaction = Transaction.buildTransaction("CL-4321-0002-0001,SGX-FU-NK-20100910", "5");
        List<Transaction> transactions = Arrays.asList(transaction);
        Mockito.when(tradingReportServiceMock.getReport()).thenReturn(transactions);

        //Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/report/json")).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.*.Client_Information", hasItem(is("CL-4321-0002-0001"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.Product_Information", hasItem(is("SGX-FU-NK-20100910"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.Total_Transaction_Amount", hasItem(is("5"))));
    }

    @Test
    public void shouldDownloadCsvForWebInvocation() throws Exception {
        //Arrange
        Transaction transaction = Transaction.buildTransaction("CL-4321-0002-0001,SGX-FU-NK-20100910", "5");
        List<Transaction> transactions = Arrays.asList(transaction);
        Mockito.when(tradingReportServiceMock.getReport()).thenReturn(transactions);

        //Act and Assert
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/report/csv").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentType(), is("text/csv"));
        assertThat(mvcResult.getResponse().getContentAsString().length(), is(106));
    }

}
