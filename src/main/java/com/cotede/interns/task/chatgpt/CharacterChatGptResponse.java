package com.cotede.interns.task.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CharacterChatGptResponse {
    @JsonProperty("id")
    private Long id;
    private String name;
    private Integer age;
    private Boolean gender;
    private String occupation;
    private Boolean isCriminal;
    private String alibi;
    private String motive;
}
