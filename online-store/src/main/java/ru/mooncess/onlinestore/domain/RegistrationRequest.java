package ru.mooncess.onlinestore.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationRequest {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
