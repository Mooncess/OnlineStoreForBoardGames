package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.domain.RegistrationRequest;
import ru.mooncess.onlinestore.entity.Basket;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.repository.BasketRepository;
import ru.mooncess.onlinestore.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private BasketRepository basketRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setBasketRepository(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public User createNewUser(RegistrationRequest registrationUserDto) {
        User user = new User();
        user.setId(registrationUserDto.getId());
        user.setFirstName(registrationUserDto.getFirstName());
        user.setLastName(registrationUserDto.getLastName());
        user.setPhoneNumber(registrationUserDto.getPhoneNumber());
        user.setPersonalDiscount((byte) 0);
        Basket basket = new Basket();
        basket.setId(user.getId());
        basketRepository.save(basket);
        user.setBasket(basket);
        return userRepository.save(user);
    }
}
