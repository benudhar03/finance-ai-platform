package com.ar.mcp.Kafka.producerconsumer;

import com.ar.mcp.Kafka.event.TransactionCreatedEvent;
import com.ar.mcp.Kafka.event.TransactionKafkaTopics;
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
                TransactionKafkaTopics.TRANSACTION_CREATED,
                event.accountNumber(),
                event
        ).whenComplete((result, exception) -> {

            if (exception != null) {
                log.error(
                        "Failed to publish TransactionCreatedEvent. " +
                                "transactionReference={}",
                        event.transactionId(),
                        exception
                );
                return;
            }
            log.info(
                    "TransactionCreatedEvent published successfully. " +
                            "transactionReference={}, partition={}, offset={}",
                    event.transactionId(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );
        });
    }
}