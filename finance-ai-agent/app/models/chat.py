# Defines request and response models for the finance chat API.
#
# Pydantic models provide validation and keep the API contract strongly typed.

from typing import Any

from pydantic import BaseModel, Field


class ChatRequest(BaseModel):
    """Request submitted by the client to the finance assistant."""

    message: str = Field(
        ...,
        min_length=1,
        description="Natural-language finance question.",
    )


class ChatResponse(BaseModel):
    """Response returned by the finance assistant."""

    response: Any