# Finance AI Platform

> Production-oriented AI finance platform built with Java 21, Spring Boot, Spring AI, OpenAI, and MCP, combining LLM-powered natural-language interactions, dynamic tool discovery, real-time financial data, and scalable event-driven services.

## 🚀 Overview

Finance AI Platform is an AI-powered financial system designed to demonstrate how modern backend architecture, LLMs, and the Model Context Protocol (MCP) can work together to build intelligent financial applications.

The platform currently consists of two independent Spring Boot applications:

```text
finance-ai-platform/
│
├── README.md
│
├── finance-mcp-server/
│   ├── pom.xml
│   ├── README.md
│   └── src/
│
├── finance-mcp-client/
│   ├── pom.xml
│   ├── README.md
│   └── src/
│
└── finance-ai-agent/
    ├── README.md
    ├── requirements.txt
    ├── .env.example
    ├── app/
    │   ├── main.py
    │   ├── api/
    │   ├── mcp/
    │   ├── models/
    │   └── ...
    └── tests/
