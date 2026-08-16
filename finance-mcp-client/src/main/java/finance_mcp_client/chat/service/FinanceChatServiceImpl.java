package finance_mcp_client.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import finance_mcp_client.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceChatServiceImpl implements FinanceChatService {

    private final FinanceAgentService financeAgentService;
    private final ObjectMapper objectMapper;

    @Override
    public ChatResponse chat(String message) {

        JsonNode response = financeAgentService.investigate(message);
        log.info("Structured response: {}", response);
        Object responseObject =
                objectMapper.convertValue(response, Object.class);
        return new ChatResponse(responseObject);
    }
}