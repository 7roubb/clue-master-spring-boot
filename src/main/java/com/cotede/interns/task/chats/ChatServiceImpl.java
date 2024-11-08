package com.cotede.interns.task.chats;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final List<ChatMessageResponseDTO> chatMessages = new ArrayList<>();

    public ChatServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO chatMessageRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sender = authentication.getName();

        ChatMessageResponseDTO message = new ChatMessageResponseDTO(
                chatMessageRequest.getRoomId(),
                sender,
                chatMessageRequest.getContent(),
                System.currentTimeMillis()
        );

        sendMessageToRoom(chatMessageRequest.getRoomId(), message);
        chatMessages.add(message);

        return message;
    }

    @Override
    public void sendMessageToRoom(Long roomId, ChatMessageResponseDTO message) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    @Override
    public List<ChatMessageResponseDTO> getRoomMessages(Long roomId) {
        return chatMessages.stream()
                .filter(msg -> msg.getRoomId().equals(roomId))
                .toList();
    }

    @Override
    public List<String> getRoomPlayers(Long roomId) {
        return new ArrayList<>();
    }
}
