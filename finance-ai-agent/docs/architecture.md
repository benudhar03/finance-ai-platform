# Finance AI Agent Architecture

Target architecture:

```text
User
  |
  v
FastAPI
  |
  v
LangGraph Finance Agent
  |
  +---- MCP ----> Java finance-mcp-server ----> Financial DB
  |
  +---- RAG ----> Vector Store
  |
  +---- Memory -> Redis
```

The implementation will be delivered incrementally.
