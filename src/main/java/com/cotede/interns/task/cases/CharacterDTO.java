package com.cotede.interns.task.cases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterDTO {
    private Long id;
    private String name;
    private Integer age;
    private Boolean gender;
    private String occupation;
    private String alibi;
    private String motive;
}
