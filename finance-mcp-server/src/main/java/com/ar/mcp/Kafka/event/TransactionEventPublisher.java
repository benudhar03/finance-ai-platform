package com.ar.mcp.Kafka.event;

public interface TransactionEventPublisher {

    void publishTransactionCreated(TransactionCreatedEvent event);
}