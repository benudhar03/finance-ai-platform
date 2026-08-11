package com.ar.mcp.transaction.domain;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.domain.CurrencyCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(
            name = "transaction_reference",
            nullable = false,
            unique = true
    )
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false
    )
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private CurrencyCode currency;

    @Column(length = 255)
    private String description;

    @Column(
            name = "transaction_date",
            nullable = false
    )
    private Instant transactionDate;

    protected Transaction() {
    }

    public Transaction(
            String transactionReference,
            Account account,
            TransactionType type,
            BigDecimal amount,
            CurrencyCode currency,
            String description,
            Instant transactionDate
    ) {
        this.transactionReference = transactionReference;
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.transactionDate = transactionDate;
    }
}