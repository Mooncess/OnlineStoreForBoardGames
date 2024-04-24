package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.domain.RegistrationRequest;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(RegistrationRequest registrationUserDto) {
        User user = new User();
        user.setId(registrationUserDto.getId());
        user.setFirstName(registrationUserDto.getFirstName());
        user.setLastName(registrationUserDto.getLastName());
        user.setPhoneNumber(registrationUserDto.getPhoneNumber());
        user.setPersonalDiscount((byte) 0);
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
