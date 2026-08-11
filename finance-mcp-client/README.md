# Finance MCP Client

AI-powered Finance MCP Client built with **Java 21**, **Spring Boot**, **Spring AI**, **OpenAI**, and the **Model Context Protocol (MCP)**.

The application provides a natural-language interface to the Finance MCP Server. It allows users to ask financial questions in plain English while the LLM dynamically selects and invokes the appropriate MCP tools.

---

## Overview

The Finance MCP Client acts as the AI-facing application in the finance system.

Instead of directly calling account or transaction APIs, the client connects to the Finance MCP Server and discovers the tools exposed by the server.

Current MCP capabilities:

- Get account balance
- Get account transactions
- Query transactions with date ranges
- Limit transaction results
- Natural-language financial queries

The client communicates with the MCP Server using **Streamable HTTP**.

---

## Architecture

```text
                         User
                           |
                           | Natural Language
                           v
                +------------------------+
                |   Finance MCP Client   |
                |       :8989            |
                +-----------+------------+
                            |
                            v
                +------------------------+
                |     Spring AI          |
                |      ChatClient        |
                +-----------+------------+
                            |
                            v
                +------------------------+
                |       OpenAI LLM       |
                +-----------+------------+
                            |
                            | Tool Selection
                            v
                +------------------------+
                |      MCP Client        |
                +-----------+------------+
                            |
                            | Streamable HTTP
                            v
                +------------------------+
                |  Finance MCP Server    |
                |       :8888             |
                +-----------+------------+
                            |
                            v
                +------------------------+
                |      PostgreSQL        |
                +------------------------+