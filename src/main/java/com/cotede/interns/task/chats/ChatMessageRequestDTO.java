package com.cotede.interns.task.chats;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDTO {
    @NotBlank
    private Long roomId;
    @NotBlank
    private String content;
}
