package com.ar.mcp.Kafka.producerconsumer;

import com.ar.mcp.Kafka.event.TransactionCreatedEvent;
import com.ar.mcp.Kafka.event.TransactionKafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionCreatedEventConsumer {

    @KafkaListener(
            topics = TransactionKafkaTopics.TRANSACTION_CREATED,
            groupId = "finance-credit-transaction-consumer"
    )
    public void consume(TransactionCreatedEvent event) {
        log.info("========== TRANSACTION CREATED EVENT RECEIVED =========={}",event);
    }


    @KafkaListener(
            topics = TransactionKafkaTopics.TRANSACTION_DEBITED,
            groupId = "finance-debit-transaction-consumer"
    )
    public void consumeDebit(TransactionCreatedEvent event) {
        log.info("========== TRANSACTION DEBITED EVENT RECEIVED =========={}",event);
    }
}