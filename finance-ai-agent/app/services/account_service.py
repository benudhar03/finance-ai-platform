# Provides application-level account operations using the Finance MCP tools.
# The service layer isolates API routes and future agents from direct MCP protocol interactions.

from typing import Any

from app.mcp.tools import FinanceMcpTools


class AccountService:
    """Application service for account-related operations."""

    def __init__(self, finance_tools: FinanceMcpTools) -> None:
        self.finance_tools = finance_tools

    async def get_balance(
        self,
        account_number: str,
    ) -> Any:
        """Retrieve an account balance from the Finance MCP server."""

        return await self.finance_tools.get_account_balance(
            account_number
        )

    async def get_transactions(
        self,
        account_number: str,
        from_date: str | None = None,
        to_date: str | None = None,
        limit: int | None = None,
    ) -> Any:
        """Retrieve account transactions from the Finance MCP server."""

        return await self.finance_tools.get_account_transactions(
            account_number,
            from_date,
            to_date,
            limit,
        )