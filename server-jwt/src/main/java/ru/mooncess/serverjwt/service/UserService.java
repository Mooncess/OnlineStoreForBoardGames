package ru.mooncess.serverjwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mooncess.serverjwt.domain.RegistrationRequest;
import ru.mooncess.serverjwt.domain.Role;
import ru.mooncess.serverjwt.domain.User;
import ru.mooncess.serverjwt.exception.AppError;
import ru.mooncess.serverjwt.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createNewUser(RegistrationRequest registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

//    public ResponseEntity<?> changeUsername(String username, String newUsername) {
//        if (!newUsername.contains("@")) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Incorrect email address"), HttpStatus.BAD_REQUEST);
//        }
//        if (findByUsername(newUsername).isPresent()) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "A user with the specified email address already exists"), HttpStatus.BAD_REQUEST);
//        }
//        Optional<User> optionalUser = findByUsername(username);
//        User user = optionalUser.get();
//        user.setUsername(newUsername);
//        userRepository.save(user);
//        return ResponseEntity.status(HttpStatus.OK).body(new UserDto(user.getId(), user.getUsername()));
//    }

//    public List<UserDto> getAllUsers() {
//        List<User> list = userRepository.findAll();
//        List<UserDto> dtoList = new ArrayList<>();
//        for (User i : list) {
//            dtoList.add(new UserDto(i.getId(), i.getUsername()));
//        }
//        return dtoList;
//    }
//
//    public Optional<UserDto> findUserById(long id) {
//        Optional<User> optional = userRepository.findById(id);
//        return optional.map(user -> new UserDto(user.getId(), user.getUsername()));
//    }

}
