# Creates and configures the FastAPI application instance.
# It registers API routes and exposes the health endpoint used to verify application availability.

from fastapi import FastAPI

from app.api.routes.chat import router as chat_router
from app.config.settings import get_settings


settings = get_settings()


app = FastAPI(
    title=settings.app_name,
    version="0.1.0",
    description="Agentic AI service for the Finance AI Platform.",
)


@app.get("/api/v1/health", tags=["Health"])
async def health():
    """Return the current application health status."""
    return {
        "status": "UP",
        "service": settings.app_name,
        "environment": settings.app_env,
    }


app.include_router(chat_router)