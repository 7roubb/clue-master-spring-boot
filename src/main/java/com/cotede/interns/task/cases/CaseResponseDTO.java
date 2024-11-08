package com.cotede.interns.task.cases;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseResponseDTO {
    private String scenario;
    private String answerParagraph;
    private List<CharacterDTO> characters;
}
