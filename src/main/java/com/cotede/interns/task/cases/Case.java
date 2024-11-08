package com.cotede.interns.task.cases;

import com.cotede.interns.task.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString
public class Case {

    private String scenario;

    @Column(name = "answer_paragraph")
    private String answerParagraph;

    @ManyToMany
    @JoinTable(
            name = "case_characters",
            joinColumns = @JoinColumn(name = "case_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private List<Character> characters = new ArrayList<>();

}
