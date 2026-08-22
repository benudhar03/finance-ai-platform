package com.ar.mcp.Kafka.event;

import com.ar.mcp.transaction.domain.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTransactionEventPublisher implements TransactionEventPublisher{

    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    @Override
    public void publishTransactionCreated(TransactionCreatedEvent event) {

        log.info(
                "Publishing TransactionCreatedEvent. " + "transactionReference={}, accountNumber={}",
                event.transactionId(),
                event.accountNumber()
        );

        String topic = resolveTopic(event.transactionType());

        kafkaTemplate.send(
                topic, event.accountNumber(), event
        ).whenComplete((result, exception) -> {
            if (exception != null) {
                log.error(
                        "Failed to publish TransactionCreatedEvent. " + "transactionReference={}",
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

    private String resolveTopic(TransactionType transactionType) {
        return switch (transactionType) {
            case CREDIT ->
                    TransactionKafkaTopics.TRANSACTION_CREATED;

            case DEBIT ->
                    TransactionKafkaTopics.TRANSACTION_DEBITED;
        };
    }

}
