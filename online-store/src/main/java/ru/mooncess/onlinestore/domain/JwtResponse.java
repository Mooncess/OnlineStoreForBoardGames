package ru.mooncess.onlinestore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}