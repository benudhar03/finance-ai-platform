package com.ar.mcp.Kafka.producer;

import com.ar.mcp.Kafka.KafkaConfig;
import com.ar.mcp.Kafka.event.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    public void publishTransactionCreated(TransactionCreatedEvent event) {
        kafkaTemplate.send(
                KafkaConfig.TRANSACTION_CREATED_TOPIC,
                event.transactionId(),
                event
        );
        log.info(
                "TransactionCreatedEvent published. transactionId={}, accountNumber={}",
                event.transactionId(),
                event.accountNumber()
        );
    }
}