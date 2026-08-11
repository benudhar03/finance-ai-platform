package finance_mcp_client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAIClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

        return chatClientBuilder
                .defaultSystem("""
                        You are a financial assistant.

                        You help users understand their financial
                        information using available finance tools.

                        Rules:
                        - Never invent financial information.
                        - Use available tools when financial data
                          is required.
                        - Never guess an account balance.
                        - Keep responses clear and concise.
                        """)
                .build();
    }
}