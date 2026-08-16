# Finance AI Platform

> An AI-powered financial investigation platform demonstrating Model Context Protocol (MCP), agentic AI, Spring Boot, Python,
FastAPI, Spring AI, OpenAI, and PostgreSQL.

The platform separates AI orchestration from financial business capabilities by exposing financial operations through an MCP server.

## 🚀 Overview

Finance AI Platform is an AI-powered financial system designed to demonstrate how modern backend architecture, LLMs, Python, Java, and the Model Context Protocol (MCP) can work together to build intelligent financial applications.

The platform follows a **multi-service architecture** where Python is responsible for the AI-facing API layer and Java provides the MCP-based financial capabilities.

The platform currently consists of three primary components:

1. **Finance MCP Server** — Java Spring Boot service that exposes financial operations as MCP tools.
2. **Finance MCP Client / AI Agent** — Java Spring Boot service responsible for interacting with the LLM and dynamically invoking MCP tools.
3. **Finance AI Agent** — Python FastAPI service that provides the external AI/chat API and communicates with the MCP layer. 

### 🏗️ High-Level Architecture

```text
                         ┌──────────────────────┐
                         │       Client         │
                         │  Swagger / UI / API  │
                         └──────────┬───────────┘
                                    │
                                    │ HTTP
                                    ▼
                    ┌─────────────────────────────┐
                    │     Finance AI Agent        │
                    │       Python / FastAPI      │
                    │                             │
                    │  • Chat API                 │
                    │  • Request Routing           │
                    │  • MCP Integration           │
                    └─────────────┬───────────────┘
                                  │
                                  │ MCP
                                  ▼
                    ┌─────────────────────────────┐
                    │    Finance MCP Client       │
                    │      Java / Spring Boot     │
                    │                             │
                    │  • Finance Agent             │
                    │  • LLM Integration           │
                    │  • Tool Discovery            │
                    │  • Tool Invocation           │
                    └─────────────┬───────────────┘
                                  │
                                  │ MCP
                                  ▼
                    ┌─────────────────────────────┐
                    │     Finance MCP Server      │
                    │      Java / Spring Boot     │
                    │                             │
                    │  • Account Tools             │
                    │  • Transaction Tools         │
                    │  • Financial Domain Logic    │
                    └─────────────┬───────────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │    Database      │
                         │                  │
                         │ Accounts         │
                         │ Transactions     │
                         └──────────────────┘
