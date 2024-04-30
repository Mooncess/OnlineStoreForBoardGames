package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.domain.RegistrationRequest;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.BasketItem;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.repository.UserRepository;

import java.util.List;
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
        user.setUsername(registrationUserDto.getUsername());
        user.setFirstName(registrationUserDto.getFirstName());
        user.setLastName(registrationUserDto.getLastName());
        user.setPhoneNumber(registrationUserDto.getPhoneNumber());
        user.setPersonalDiscount((byte) 0);
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public List<BasketItem> getUserBasket(User user) {
        return user.getBasketList();
    }

    public List<Article> getUserWishlist(User user) {
        return user.getWishList();
    }
    public boolean changePhoneNumber(String phone, User user) {
        try {
            user.setPhoneNumber(phone);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
