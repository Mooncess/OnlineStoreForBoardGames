package ru.mooncess.serverjwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForApp {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
