package com.abnamro.report.streaming;

import com.abnamro.report.api.TradingReportService;
import com.abnamro.report.api.Transaction;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static com.abnamro.report.streaming.TradingRecordTestFixture.KEY1;
import static com.abnamro.report.streaming.TradingRecordTestFixture.KEY2;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"test"})
@DirtiesContext
@TestPropertySource(locations = "classpath:application-test.properties")
public class TradingRecordProcessorTest {

    @Autowired
    private TradingRecordStreamProcessor tradingRecordStreamProcessor;

    @Autowired
    private TradingReportService tradingReportService;

    @Autowired
    private AppConfigs appConfigs;

    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;
    private Serde<String> stringSerde = Serdes.String();
    private KeyValueStore<String, String> store;
    private TopologyTestDriver testDriver;
    private String sourceTopicName;
    private String targetTopicName;

    @Before
    public void setUp(){
        sourceTopicName = appConfigs.getSourceTopicName();
        targetTopicName = appConfigs.getTargetTopicName();
        Properties config = getEmbeddedKafkaProperties();
        testDriver = new TopologyTestDriver(tradingRecordStreamProcessor.createTopology(), config);
        inputTopic = testDriver.createInputTopic(sourceTopicName, stringSerde.serializer(), stringSerde.serializer());
        outputTopic = testDriver.createOutputTopic(targetTopicName, stringSerde.deserializer(), stringSerde.deserializer());
        store = testDriver.getKeyValueStore(appConfigs.getStateStore());
    }

    private Properties getEmbeddedKafkaProperties() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, appConfigs.getApplicationId());
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigs.getBootstrapServer());
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return config;
    }

    @Test
    public void shouldAggregateByCustomerInfoAndProductInfo() throws IOException, InterruptedException {

        //Arrange
        List<String> dataList = TradingRecordTestFixture.getTestData();
        dataList.forEach( data -> sendDataToKafka(data));

        String key = outputTopic.readKeyValue().key;
        System.out.println(key);
        System.out.println(store.get(KEY1));
        System.out.println(store.get(KEY2));

        //Assert
        assertThat(store.get(KEY1), is("3"));
        assertThat(store.get(KEY2), is("4"));
    }

    //@Test
    public void shouldGetReportWithCustomerAndProductInfo(){
        //Arrange
        List<String> dataList = TradingRecordTestFixture.getTestData();
        dataList.forEach( data -> sendDataToKafka(data));

        //Act
        List<Transaction> tradingReport = tradingReportService.getReport();

        //Assert
        assertThat(tradingReport, notNullValue());
        assertThat(tradingReport.size(), is(2));
        assertThat(tradingReport, containsInAnyOrder(hasProperty("Client_Information", is("CL-4321-0002-0001")),
                hasProperty("Client_Information", is("CL-1234-0002-0001"))));

        assertThat(tradingReport, containsInAnyOrder(hasProperty("Product_Information", is("SGX-FU-NK-20100910")),
                hasProperty("Product_Information", is("SGX-FU-NK-20100910"))));

        assertThat(tradingReport, hasItems(Transaction.buildTransaction(KEY1, "3"),
                Transaction.buildTransaction(KEY2, "4")));
    }

    private void sendDataToKafka(String data) {
        inputTopic.pipeInput(data);
    }

    @After
    public void tearDown() {
        try {
            testDriver.close();
            Thread.sleep(10000);
        } catch (Exception e) {

        }
    }
}
