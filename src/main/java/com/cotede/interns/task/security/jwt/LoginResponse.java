package com.cotede.interns.task.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private String username;
    private Long id ;
}
