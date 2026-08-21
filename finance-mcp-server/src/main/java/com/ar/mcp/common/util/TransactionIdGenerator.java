package com.ar.mcp.common.util;

import java.util.concurrent.atomic.AtomicLong;

public final class TransactionIdGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(10010);

    private TransactionIdGenerator() {
    }

    public static String nextTransactionId() {
        return "TXN-" + SEQUENCE.incrementAndGet();
    }
}