# Defines structured models used when representing MCP tools and their results.
# Keeping MCP data structured prevents raw protocol responses from leaking into business logic.

from typing import Any
from pydantic import BaseModel, Field


class McpToolInfo(BaseModel):
    """Represents a tool discovered from the MCP server."""

    name: str
    description: str | None = None
    input_schema: dict[str, Any] = Field(default_factory=dict)


class McpToolResult(BaseModel):
    """Represents the normalized result returned from an MCP tool."""

    tool_name: str
    content: list[Any] = Field(default_factory=list)
    is_error: bool = False