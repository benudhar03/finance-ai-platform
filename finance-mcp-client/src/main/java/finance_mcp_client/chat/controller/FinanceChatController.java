package finance_mcp_client.chat.controller;

import finance_mcp_client.chat.dto.ChatRequest;
import finance_mcp_client.chat.dto.ChatResponse;
import finance_mcp_client.chat.service.FinanceChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/finance")
public class FinanceChatController {

    private final FinanceChatService financeChatService;


    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(financeChatService.chat(request.message()));
    }
}