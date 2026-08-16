package finance_mcp_client.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public FinanceAgentService(ChatClient chatClient, ToolCallbackProvider mcpTools, ObjectMapper objectMapper) {

        log.info("========== FinanceAgentService CREATED ==========");
        this.chatClient = chatClient;
        this.mcpTools = mcpTools;
        this.objectMapper = objectMapper;
        log.info("========== MCP TOOLS ==========");
        Arrays.stream(mcpTools.getToolCallbacks())
                .map(ToolCallback::getToolDefinition)
                .forEach(tool ->
                        log.info(
                                "MCP Tool: {}",
                                tool.name()
                        )
                );
        log.info("==============================================");
    }

    public JsonNode investigate(String message) {

        log.info("Starting finance agent request: {}", message);
        String response = chatClient
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
                        9. This agent is strictly READ-ONLY.
                        10. Never modify, create, delete, transfer, or freeze
                            financial data.

                        RESPONSE FORMAT:

                        Return ONLY valid JSON.

                        Do not return markdown.
                        Do not return ```json.
                        Do not return explanations outside the JSON.

                        Preserve the structure and values returned by the MCP tools.

                        For account balance requests, return the MCP result
                        as JSON.

                        For transaction requests, return the MCP result
                        as JSON.

                        Example:

                        {
                          "accountNumber": "ACC-1002",
                          "currency": "EUR",
                          "transactions": []
                        }
                        """)
                .user(message)
                .tools(mcpTools)
                .call()
                .content();

        log.info("Raw LLM response: {}", response);
        try {
            return objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            log.warn("LLM response is not valid JSON. Returning as text.");
            return objectMapper.createObjectNode()
                    .put("message", response);
        }
    }
}