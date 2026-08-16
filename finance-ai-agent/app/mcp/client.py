# Provides the connection layer between the Python application and the Java MCP server.
# It manages MCP sessions and exposes tool discovery and invocation capabilities.

from contextlib import asynccontextmanager
from typing import Any, AsyncIterator

from mcp import ClientSession
from mcp.client.streamable_http import streamable_http_client

from app.config.settings import get_settings


class McpClient:
    """Client used to communicate with the Finance MCP server."""

    def __init__(self) -> None:
        self.settings = get_settings()

    @asynccontextmanager
    async def session(self) -> AsyncIterator[ClientSession]:
        """Create and manage a Streamable HTTP MCP session."""

        async with streamable_http_client(
            self.settings.mcp_server_url
        ) as streams:

            read_stream, write_stream = streams

            async with ClientSession(
                read_stream,
                write_stream,
            ) as session:

                await session.initialize()

                yield session

    async def list_tools(self) -> Any:
        """Discover tools currently exposed by the MCP server."""

        async with self.session() as session:
            return await session.list_tools()

    async def call_tool(
        self,
        tool_name: str,
        arguments: dict[str, Any],
    ) -> Any:
        """Invoke an MCP tool with the supplied arguments."""

        async with self.session() as session:
            return await session.call_tool(
                tool_name,
                arguments,
            )