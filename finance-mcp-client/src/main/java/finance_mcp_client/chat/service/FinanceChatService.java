package finance_mcp_client.chat.service;


import finance_mcp_client.chat.dto.ChatResponse;

public interface FinanceChatService {

    ChatResponse chat(String message);
}