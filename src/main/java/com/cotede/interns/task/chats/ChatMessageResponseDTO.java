package com.cotede.interns.task.chats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDTO {
    private Long roomId;
    private String sender;
    private String content;
    private Long timestamp;
}