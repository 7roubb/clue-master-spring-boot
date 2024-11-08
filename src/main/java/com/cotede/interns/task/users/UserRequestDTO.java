package com.cotede.interns.task.users;

import com.cotede.interns.task.common.OnCreate;
import com.cotede.interns.task.common.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
@PhoneOrEmailValid(groups = OnCreate.class)
public class UserRequestDTO {
    @NotNull(groups = {OnUpdate.class}, message = "{user.id.notnull}")
    @Positive(message = "{user.id.positive}")
    private Long id;

    @NotNull(groups = OnCreate.class, message = "{user.username.notnull}")
    @NotBlank(message = "{user.username.notblank}")
    @Size(min = 3, max = 50, message = "{user.username.size}")
    private String userName;

    @NotNull(groups = OnCreate.class, message = "{user.password.notnull}")
    @NotBlank(groups = OnCreate.class, message = "{user.password.notblank}")
    @Size(min = 8, message = "{user.password.size}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "{user.password.pattern}")
    private String password;

    @NotBlank(message = "{user.email.notblank}", groups = OnCreate.class)
    @Email(message = "{user.email.valid}")
    private String email;

    @NotBlank(message = "{user.phone.notblank}", groups = OnCreate.class)
    @Pattern(regexp = "^(\\+970|970|0)(59[245789]|56[26789])[0-9]{6}$", groups = {OnCreate.class, OnUpdate.class}, message = "{user.phone.valid}")
    private String phoneNumber;

    @NotNull(message = "{user.role.notnull}", groups = OnCreate.class)
    private RoleType role;
}
