package com.cotede.interns.task.users;

import com.cotede.interns.task.exceptions.CustomExceptions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Optional;
import java.util.stream.Stream;

public class PhoneOrEmailValidator implements ConstraintValidator<PhoneOrEmailValid, UserRequestDTO> {


    @Override
    public void initialize(PhoneOrEmailValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserRequestDTO dto, ConstraintValidatorContext context) {
        boolean phoneNumberIsValid = dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank();
        boolean emailIsValid = dto.getEmail() != null && !dto.getEmail().isBlank();
        if (!phoneNumberIsValid && !emailIsValid) {
            throw new CustomExceptions.PhoneOrEmailRequiredException();
        }
        return true;
    }
}
