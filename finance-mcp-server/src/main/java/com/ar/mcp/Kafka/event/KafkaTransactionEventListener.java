package com.ar.mcp.Kafka.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTransactionEventListener {

    private final KafkaTransactionEventPublisher transactionEventPublisher;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleTransactionCreated(TransactionCreatedEvent event) {

        log.info(
                "Database transaction committed. " +
                "Publishing transaction event to Kafka. transactionReference={}",
                event.transactionId()
        );
        transactionEventPublisher.publishTransactionCreated(event);
    }
}