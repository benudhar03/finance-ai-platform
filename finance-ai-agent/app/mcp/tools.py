# Provides application-level operations for interacting with MCP tools.
#
# This layer keeps MCP protocol details out of the FastAPI routes.

import json
from typing import Any

from app.mcp.client import McpClient


class FinanceMcpTools:
    """Application service responsible for MCP tool operations."""

    def __init__(self) -> None:
        self.client = McpClient()

    async def list_tools(self) -> Any:
        """Return tools currently exposed by the MCP server."""

        return await self.client.list_tools()

    async def process_request(self, message: str) -> Any:
        """
        Process a finance request using the MCP server.

        Phase 2 uses simple routing temporarily.
        Agentic tool selection will be introduced in the next phase.
        """

        message_lower = message.lower()

        # ---------------------------------------------------------
        # Account Balance
        # ---------------------------------------------------------
        if "balance" in message_lower:

            result = await self.client.call_tool(
                "getAccountBalance",
                {
                    "accountNumber": "ACC-1002",
                },
            )

            return self._extract_result(result)

        # ---------------------------------------------------------
        # Account Transactions
        # ---------------------------------------------------------
        if "transaction" in message_lower:

            result = await self.client.call_tool(
                "getAccountTransactions",
                {
                    "request": {
                        "accountNumber": "ACC-1002",
                        "limit": 5,
                    }
                },
            )

            return self._extract_result(result)

        # ---------------------------------------------------------
        # Unsupported request
        # ---------------------------------------------------------
        return {
            "message": (
                "I could not determine which finance operation is required. "
                "Please ask about an account balance or transactions."
            )
        }

    @staticmethod
    def _extract_result(result: Any) -> Any:
        """
        Extract JSON returned inside MCP TextContent.

        MCP CallToolResult looks approximately like:

        CallToolResult(
            content=[
                TextContent(
                    type="text",
                    text='{"accountNumber":"ACC-1002", ...}'
                )
            ]
        )

        This method extracts the text and converts it into
        a native Python JSON structure.
        """

        if not result or not getattr(result, "content", None):
            return {}

        for content in result.content:

            if getattr(content, "type", None) != "text":
                continue

            text = getattr(content, "text", None)

            if not text:
                continue

            try:
                return json.loads(text)

            except json.JSONDecodeError:
                return {
                    "message": text
                }

        return {}