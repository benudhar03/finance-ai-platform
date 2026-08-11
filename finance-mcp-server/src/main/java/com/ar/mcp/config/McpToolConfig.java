package com.ar.mcp.config;

import com.ar.mcp.tools.AccountTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfig {

    @Bean
    public ToolCallbackProvider financeTools(AccountTools accountTools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(accountTools)
                .build();
    }
}