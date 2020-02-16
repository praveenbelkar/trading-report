package com.abnamro.report.api;

import com.abnamro.report.streaming.AppConfigs;
import com.abnamro.report.streaming.TradingRecordStreamProcessor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradingReportService {

    private static final Logger logger = LoggerFactory.getLogger(TradingReportService.class);

    @Autowired
    private KafkaStreams tradingRecordStream;

    @Autowired
    private AppConfigs appConfigs;

    public List<Transaction> getReport() {

        logger.info("Getting the report for the transactions....");
        List<Transaction> transactions = new ArrayList<>();

        if(TradingRecordStreamProcessor.active) {
            ReadOnlyKeyValueStore<String, String> keyValueStore =
                    tradingRecordStream.store(appConfigs.getStateStore(), QueryableStoreTypes.keyValueStore());
            KeyValueIterator<String, String> range = keyValueStore.all();
            Transaction transaction = null;
            while (range.hasNext()) {
                KeyValue<String, String> next = range.next();
                String key = next.key;
                String value = next.value;
                logger.debug("Getting Record from store: " + key + ": " + value);
                transaction = Transaction.buildTransaction(key, value);
                transactions.add(transaction);
            }
            // close the iterator to release resources
            range.close();
        } else {
            logger.error("Invalid state for stream.....");
        }
        return transactions;
    }

}
