package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.dto.ChatDTO;
import lk.ijse.DreamsCake.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ChatDTO> askChatbot(@RequestBody ChatDTO chatDTO) {
        ChatDTO responseDTO = chatbotService.processChatbotMessage(chatDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
