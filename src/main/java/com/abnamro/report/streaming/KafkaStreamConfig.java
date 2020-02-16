package com.abnamro.report.streaming;

import org.apache.kafka.streams.KafkaStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaStreamConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamConfig.class);

    @Autowired
    private TradingRecordStreamProcessor tradingRecordStreamProcessor;

    @Bean
    public KafkaStreams tradingRecordStream() {
        logger.info("stream creation begins.......");
        return tradingRecordStreamProcessor.processTradingRecord();
    }
}
