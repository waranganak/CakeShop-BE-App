package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.ChatDTO;

public interface ChatbotService {
    ChatDTO processChatbotMessage(ChatDTO chatDTO);
}