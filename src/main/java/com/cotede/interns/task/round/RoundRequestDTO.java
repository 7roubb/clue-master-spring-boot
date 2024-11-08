package com.cotede.interns.task.round;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundRequestDTO {
    private Long gameId;
    private Integer roundNumber;
}
