package com.cotede.interns.task.cases;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Character {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;
    private Boolean gender;

    @NotBlank(message = "Occupation is required")
    private String occupation;
    private Boolean isCriminal;
    private String alibi;
    private String motive;
}
