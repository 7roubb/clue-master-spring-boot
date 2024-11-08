package com.cotede.interns.task.cases;

import com.cotede.interns.task.round.RoundAnswerRequestDTO;
import com.cotede.interns.task.users.User;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public class CaseMapper {


    public static CaseResponseDTO toCaseResponseDTO(Case caseEntity) {

        List<CharacterDTO> characterDTOList = caseEntity.getCharacters().stream()
                .map(CaseMapper::toCharacterDTO)
                .collect(Collectors.toList());

        return CaseResponseDTO.builder()
                .scenario(caseEntity.getScenario())
                .characters(characterDTOList)
                .build();
    }

    public static CharacterDTO toCharacterDTO(Character character) {
        return CharacterDTO.builder()
                .id(character.getId())
                .name(character.getName())
                .age(character.getAge())
                .gender(character.getGender())
                .occupation(character.getOccupation())
                .alibi(character.getAlibi())
                .motive(character.getMotive())
                .build();
    }

    public static Answer toAnswerEntity(RoundAnswerRequestDTO roundAnswerRequestDTO, User user) {
        return Answer.builder()
                .answer(roundAnswerRequestDTO.getAnswer())
                .user(user)
                .points(0)
                .build();
    }

}
