package ru.mooncess.serverjwt.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mooncess.serverjwt.domain.Role;
import ru.mooncess.serverjwt.domain.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final List<User> users;

    public UserService() {
        this.users = List.of(
                new User("anatoly", "12345", "Анатолий", "Шулик", Collections.singleton(Role.ADMIN)),
                new User("vlad", "123", "Влад", "Власов", Collections.singleton(Role.USER))
        );
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }

}
