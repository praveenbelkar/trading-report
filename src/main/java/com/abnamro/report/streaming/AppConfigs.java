package com.abnamro.report.streaming;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfigs {
    @Value("${application.id}")
    private String applicationId;

    @Value("${kafka.host:localhost:9092}")
    private String bootstrapServer;

    @Value("${source.topic}")
    private String sourceTopicName;

    @Value("${target.topic}")
    private String targetTopicName;

    @Value("${state.store}")
    private String stateStore;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public String getSourceTopicName() {
        return sourceTopicName;
    }

    public void setSourceTopicName(String sourceTopicName) {
        this.sourceTopicName = sourceTopicName;
    }

    public String getTargetTopicName() {
        return targetTopicName;
    }

    public void setTargetTopicName(String targetTopicName) {
        this.targetTopicName = targetTopicName;
    }

    public String getStateStore() {
        return stateStore;
    }

    public void setStateStore(String stateStore) {
        this.stateStore = stateStore;
    }

}
