package com.ar.mcp.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String TRANSACTION_CREATED_TOPIC = "finance.transaction.created";

    @Bean
    public NewTopic transactionCreatedTopic() {

        return TopicBuilder
                .name(TRANSACTION_CREATED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}