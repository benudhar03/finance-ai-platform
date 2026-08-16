# Defines the initial API endpoints used to verify Python-to-MCP connectivity.
#
# These endpoints will evolve into agent-driven workflows as the finance agent is implemented.

from fastapi import APIRouter, Depends

from app.api.dependencies import get_finance_tools
from app.mcp.tools import FinanceMcpTools
from app.models.chat import ChatRequest, ChatResponse


router = APIRouter(
    prefix="/api/v1/chat",
    tags=["Chat"],
)


@router.get("/ping")
async def ping():
    """Verify that the chat API is available."""

    return {
        "message": "Finance AI Agent chat API is ready"
    }


@router.get("/mcp/tools")
async def list_mcp_tools(
    tools: FinanceMcpTools = Depends(get_finance_tools),
):
    """Return tools dynamically discovered from the Java MCP server."""

    result = await tools.list_tools()

    return {
        "tools": [
            {
                "name": tool.name,
                "description": tool.description,
                "inputSchema": tool.inputSchema,
            }
            for tool in result.tools
        ]
    }


@router.post("", response_model=ChatResponse)
async def chat(
    request: ChatRequest,
    tools: FinanceMcpTools = Depends(get_finance_tools),
):
    """Process a finance request through the MCP server."""

    result = await tools.process_request(request.message)

    return ChatResponse(
        response=result,
    )