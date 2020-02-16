package com.abnamro.report.streaming;

import com.abnamro.report.util.TransactionUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TradingRecordStreamProcessor {
    //private static final Logger logger = LogManager.getLogger();
    private static final Logger logger = LoggerFactory.getLogger(TradingRecordStreamProcessor.class);

    @Autowired
    private TransactionUtil transactionUtil;

    @Autowired
    private AppConfigs appConfigs;

    public static boolean active;

    public KafkaStreams processTradingRecord() {
        KafkaStreams streams = new KafkaStreams(createTopology(), createStreamProperties());
        streams.setStateListener((currentState, prevState) -> {
            logger.info("***************State Changing to " + currentState + " from " + prevState);
            setActive(currentState == KafkaStreams.State.RUNNING);
        });
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping Stream");
            streams.close();
        }));

        return streams;
    }

    private static void setActive(boolean active) {
        TradingRecordStreamProcessor.active = active;
    }

    Properties createStreamProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appConfigs.getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appConfigs.getBootstrapServer());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> tradingRecordStream = builder.stream(appConfigs.getSourceTopicName());
        tradingRecordStream
                .map(rekeyToClientAndProductInfo())
                .groupByKey()
                .reduce(addAsLongAndConvertToString(), Materialized.as(appConfigs.getStateStore()))
                .toStream()
                .to(appConfigs.getTargetTopicName());

        return builder.build();
    }

    private KeyValueMapper<String, String, KeyValue<? extends String, ? extends String>> rekeyToClientAndProductInfo() {
        return (key,tradingRecord) -> new KeyValue<>(transactionUtil.createKey(tradingRecord), transactionUtil.createTotalTransaction(tradingRecord));
    }

    private Reducer<String> addAsLongAndConvertToString() {
        return (oldV, newV) -> {
            Long long1 = Long.parseLong(oldV);
            Long long2 = Long.parseLong(newV);
            return String.valueOf(long1 + long2);
        };
    }

}
