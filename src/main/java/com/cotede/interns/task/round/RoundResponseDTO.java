package com.cotede.interns.task.round;

import com.cotede.interns.task.cases.CaseResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoundResponseDTO {
    private Long id;
    private Integer roundNumber;
    private boolean completed;
    private List<CaseResponseDTO> cases;
}
