package com.cotede.interns.task.chats;

import java.util.List;

public interface ChatService {
    ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO chatMessageRequest);
    void sendMessageToRoom(Long roomId, ChatMessageResponseDTO message);
    List<ChatMessageResponseDTO> getRoomMessages(Long roomId);
    List<String> getRoomPlayers(Long roomId);
}
