# Finance MCP Server

MCP Server for a real-time Finance application built using Java 21, Spring Boot, Spring AI, MCP, JPA, and PostgreSQL.

The application exposes financial capabilities as MCP tools that can be consumed dynamically by an MCP client or AI application.

---

## 🚀 Overview

The Finance MCP Server provides financial data and operations through the Model Context Protocol (MCP).

Currently implemented capabilities:

- Get account balance
- Get account transactions
- Account validation
- Structured financial responses
- Read-only MCP operations
- Transaction filtering by date range
- Transaction result limiting
- PostgreSQL persistence

The server exposes MCP tools over Streamable HTTP.

---

## 🏗️ Architecture

```text
                    ┌─────────────────────────┐
                    │      MCP Client         │
                    │      Port: 8989         │
                    └────────────┬────────────┘
                                 │
                                 │ MCP / Streamable HTTP
                                 ▼
                    ┌─────────────────────────┐
                    │    Finance MCP Server   │
                    │      Port: 8888         │
                    └────────────┬────────────┘
                                 │
                ┌────────────────┴────────────────┐
                │                                 │
                ▼                                 ▼
       ┌──────────────────┐             ┌──────────────────┐
       │   AccountTools   │             │ TransactionService│
       └────────┬─────────┘             └─────────┬────────┘
                │                                 │
                ▼                                 ▼
       ┌──────────────────┐             ┌──────────────────┐
       │ AccountService   │             │ TransactionRepo  │
       └────────┬─────────┘             └─────────┬────────┘
                │                                 │
                └────────────────┬────────────────┘
                                 ▼
                    ┌─────────────────────────┐
                    │       PostgreSQL        │
                    └─────────────────────────┘

```
finance-mcp-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ar/mcp/
│   │   │       │
│   │   │       ├── account/
│   │   │       │   ├── domain/
│   │   │       │   │   ├── Account.java
│   │   │       │   │   ├── AccountStatus.java
│   │   │       │   │   └── CurrencyCode.java
│   │   │       │   │
│   │   │       │   ├── dto/
│   │   │       │   ├── repository/
│   │   │       │   └── service/
│   │   │       │
│   │   │       ├── transaction/
│   │   │       │   ├── domain/
│   │   │       │   │   ├── Transaction.java
│   │   │       │   │   └── TransactionType.java
│   │   │       │   │
│   │   │       │   ├── dto/
│   │   │       │   ├── repository/
│   │   │       │   └── service/
│   │   │       │
│   │   │       ├── mcp/
│   │   │       │   └── config/
│   │   │       │
│   │   │       ├── tools/
│   │   │       │   └── AccountTools.java
│   │   │       │
│   │   │       └── FinanceMcpServerApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.yml
│   │
│   └── test/
│
├── pom.xml
└── README.md
````