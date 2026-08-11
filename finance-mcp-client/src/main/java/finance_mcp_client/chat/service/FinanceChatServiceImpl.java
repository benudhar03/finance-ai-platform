package finance_mcp_client.chat.service;

import finance_mcp_client.chat.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import org.springframework.ai.tool.ToolCallback;

import java.util.Arrays;

@Service
public class FinanceChatServiceImpl implements FinanceChatService {

    private final ChatClient chatClient;
    private final ToolCallbackProvider mcpTools;

    public FinanceChatServiceImpl(ChatClient chatClient, ToolCallbackProvider mcpTools) {
        System.out.println("========== FinanceChatServiceImpl CREATED ==========");

        this.chatClient = chatClient;
        this.mcpTools = mcpTools;

        System.out.println("========== MCP TOOLS ==========");
        Arrays.stream(mcpTools.getToolCallbacks())
                .map(ToolCallback::getToolDefinition)
                .forEach(tool ->
                        System.out.println("MCPTool: " + tool.name())
                );
        System.out.println("===============================");
    }

    @Override
    public ChatResponse chat(String message) {

        String response = chatClient
                .prompt()
                .user(message)
                .tools(mcpTools)
                .call()
                .content();

        return new ChatResponse(response);
    }
}