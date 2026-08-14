package finance_mcp_client.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class FinanceAgentService {

    private final ChatClient chatClient;
    private final ToolCallbackProvider mcpTools;

    public FinanceAgentService(
            ChatClient chatClient,
            ToolCallbackProvider mcpTools) {

        log.info("========== FinanceAgentService CREATED ==========");

        this.chatClient = chatClient;
        this.mcpTools = mcpTools;

        log.info("========== MCP TOOLS ==========");

        Arrays.stream(mcpTools.getToolCallbacks())
                .map(ToolCallback::getToolDefinition)
                .forEach(tool ->
                        log.info("MCP Tool: {}", tool.name())
                );

        log.info("==============================================");
    }

    public String investigate(String message) {

        log.info("Starting finance agent request: {}", message);

        return chatClient
                .prompt()
                .system("""
                        You are a financial investigation agent.

                        Your job is to investigate financial account questions
                        using the available MCP tools.

                        IMPORTANT RULES:

                        1. Always use MCP tools when financial data is required.
                        2. Never invent account, balance, currency, or transaction data.
                        3. Never invent transaction dates.
                        4. If the user does not provide a date range,
                           do not create one.
                        5. When the user asks for recent transactions,
                           retrieve the latest available transactions.
                        6. You may call multiple MCP tools when required.
                        7. Analyze the results returned by the tools before
                           producing the final answer.
                        8. Base your conclusions only on retrieved data.
                        9. Clearly distinguish facts from your analysis.
                        10. This agent is strictly READ-ONLY.
                        11. Never modify, create, delete, transfer, or freeze
                            financial data.
                                               \s
                        When investigating an account, prefer this approach:
                        - Retrieve the account balance.
                        - Retrieve the relevant transactions.
                        - Analyze the returned transaction data.
                        - Provide a concise investigation summary.
                       \s""")
                .user(message)
                .tools(mcpTools)
                .call()
                .content();
    }
}