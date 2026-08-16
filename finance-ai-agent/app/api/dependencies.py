# Defines reusable application dependencies for FastAPI routes.
# MCP clients and application services are constructed here so routes remain focused on HTTP concerns.

from functools import lru_cache

from app.mcp.client import McpClient
from app.mcp.tools import FinanceMcpTools
from app.services.account_service import AccountService


@lru_cache
def get_mcp_client() -> McpClient:
    """Return the shared MCP client instance."""

    return McpClient()


@lru_cache
def get_finance_tools() -> FinanceMcpTools:
    """Return the Finance MCP tool adapter."""

    return FinanceMcpTools()


@lru_cache
def get_account_service() -> AccountService:
    """Return the account application service."""

    return AccountService(
        get_finance_tools()
    )