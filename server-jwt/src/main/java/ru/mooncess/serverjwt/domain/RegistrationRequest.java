package ru.mooncess.serverjwt.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
