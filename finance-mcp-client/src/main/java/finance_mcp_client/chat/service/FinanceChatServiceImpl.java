package finance_mcp_client.chat.service;

import finance_mcp_client.chat.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceChatServiceImpl implements FinanceChatService {

    private final FinanceAgentService financeAgentService;

    @Override
    public ChatResponse chat(String message) {
        String response = financeAgentService.investigate(message);
        return new ChatResponse(response);
    }
}