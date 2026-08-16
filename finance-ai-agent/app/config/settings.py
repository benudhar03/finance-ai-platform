# Defines strongly typed application settings loaded from environment variables.
# Centralizing configuration keeps API, agent, MCP, and other components decoupled from environment handling.

from functools import lru_cache
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    """Application configuration loaded from environment variables."""

    app_name: str = "finance-ai-agent"
    app_env: str = "local"
    log_level: str = "INFO"

    openai_api_key: str = ""
    openai_model: str = "gpt-4.1-mini"

    mcp_server_url: str = "http://localhost:8888/mcp"

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        case_sensitive=False,
        extra="ignore",
    )


@lru_cache
def get_settings() -> Settings:
    """Return a cached Settings instance for the application."""
    return Settings()