package com.cotede.interns.task.users;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneOrEmailValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneOrEmailValid {
    String message() default "Either phone number or email must be provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}