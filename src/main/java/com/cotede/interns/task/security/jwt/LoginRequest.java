package com.cotede.interns.task.security.jwt;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequest {
    private String userName;
    private String password;
}
