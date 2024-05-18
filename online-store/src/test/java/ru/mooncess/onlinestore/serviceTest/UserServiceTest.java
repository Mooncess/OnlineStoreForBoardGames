package ru.mooncess.onlinestore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mooncess.onlinestore.domain.RegistrationRequest;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.BasketItem;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.repository.OrderRepository;
import ru.mooncess.onlinestore.repository.UserRepository;
import ru.mooncess.onlinestore.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @BeforeEach
    void setUp() {
        userService = new UserService();
        userService.setUserRepository(userRepository);
    }
    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("alice");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByUsername("alice");

        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void testCreateNewUser() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("john_doe");
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setPhoneNumber("123456789");

        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("123456789");
        user.setPersonalDiscount((byte) 0);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User savedUser = userService.createNewUser(registrationRequest);

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setUsername("alice");
        User user2 = new User();
        user2.setUsername("bob");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.findAllUsers();

        assertEquals(2, allUsers.size());
        assertEquals(user1.getUsername(), allUsers.get(0).getUsername());
        assertEquals(user2.getUsername(), allUsers.get(1).getUsername());
    }

    @Test
    public void testGetUserBasket() {
        User user = new User();
        BasketItem item = new BasketItem();
        user.setBasketList(new ArrayList<>());
        user.getBasketList().add(item);

        List<BasketItem> basketItems = userService.getUserBasket(user);

        assertEquals(1, basketItems.size());
        assertEquals(item, basketItems.get(0));
    }

    @Test
    public void testGetUserWishlist() {
        User user = new User();
        Article article = new Article();
        user.setWishList(new ArrayList<>());
        user.getWishList().add(article);

        List<Article> wishlist = userService.getUserWishlist(user);

        assertEquals(1, wishlist.size());
        assertEquals(article, wishlist.get(0));
    }

    @Test
    public void testChangePhoneNumber() {
        User user = new User();
        user.setPhoneNumber("123456789");

        String newPhoneNumber = "987654321";

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        boolean result = userService.changePhoneNumber(newPhoneNumber, user);

        assertEquals(true, result);
        assertEquals(newPhoneNumber, user.getPhoneNumber());
    }

}
