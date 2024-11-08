package com.cotede.interns.task.cases;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CaseRequestDTO {
    @NotBlank
    private Long roomId;
    private CharacterDTO characterDTO;
}
