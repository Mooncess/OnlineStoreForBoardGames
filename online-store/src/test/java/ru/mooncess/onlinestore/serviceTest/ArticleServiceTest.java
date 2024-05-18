package ru.mooncess.onlinestore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.BasketItem;
import ru.mooncess.onlinestore.entity.Category;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.mappers.ArticleMapper;
import ru.mooncess.onlinestore.repository.ArticleRepository;
import ru.mooncess.onlinestore.repository.BasketItemRepository;
import ru.mooncess.onlinestore.repository.UserRepository;
import ru.mooncess.onlinestore.service.ArticleService;
import ru.mooncess.onlinestore.service.ImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;
    private ArticleMapper mapper;

    private ArticleService articleService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = mock(ArticleMapper.class);
        articleService = new ArticleService(articleRepository, basketItemRepository, userRepository, imageService);
    }

    @Test
    public void testCreateArticle() {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        List<Long> listDto = new ArrayList<>();
        listDto.add(1L);
        List<Category> list = new ArrayList<>();
        list.add(category);

        ArticleCreateDTO articleCreateDTO = new ArticleCreateDTO();
        articleCreateDTO.setName("test_article");
        articleCreateDTO.setCategory(listDto);
        MultipartFile image = null;

        Article articleEntity = new Article();
        articleEntity.setName("test_article");
        articleEntity.setCategory(list);

        Article newArticle = new Article();
        newArticle.setName("test_article");
        newArticle.setCategory(list);
        newArticle.setId(1L);

        when(imageService.addImage(articleCreateDTO.getName(), image)).thenReturn(null);
        when(mapper.articleCreateDtoToEntity(articleCreateDTO)).thenReturn(articleEntity);
        when(articleRepository.save(articleEntity)).thenReturn(newArticle);


        // Act
        Optional<Article> createdArticle = articleService.createArticle(articleCreateDTO, image);

        System.out.println(createdArticle.isPresent());

        // Assert
        assertTrue(createdArticle.isPresent());
        assertEquals(1L, createdArticle.get().getId());
    }

    @Test
    public void testIncreaseCountOfBasketItem() {
        // Arrange
        Article article = new Article();
        article.setId(1L);
        BasketItem basketItem = new BasketItem();
        basketItem.setId(1L);
        basketItem.setArticle(article);
        basketItem.setQuantity(1);

        User user = new User();
        user.setId(1L);
        user.setBasketList(new ArrayList<>());
        user.getBasketList().add(basketItem);

        BasketItem basketItem1 = new BasketItem();
        basketItem1.setId(2L);
        basketItem1.setArticle(article);
        basketItem1.setQuantity(2);

        User user2 = new User();
        user2.setId(1L);
        user2.setBasketList(new ArrayList<>());
        user2.getBasketList().add(basketItem1);


        when(userRepository.getById(1L)).thenReturn(user);
        when(articleRepository.getById(1L)).thenReturn(article);
        when(basketItemRepository.getByArticleAndQuantity(article, 2)).thenReturn(Optional.of(basketItem1));
        when(userRepository.save(user)).thenReturn(user2);

        // Add an item to the basket
        articleService.addToBasket(1L, user);

        // Act
        boolean increased = articleService.increaseCountOfBasketItem(1L, user);

        // Assert
        assertTrue(increased);
        assertEquals(2, user2.getBasketList().get(0).getQuantity());
    }

    @Test
    public void testDecreaseCountOfBasketItem() {
        // Arrange
        Article article = new Article();
        article.setId(1L);
        BasketItem basketItem = new BasketItem();
        basketItem.setId(1L);
        basketItem.setArticle(article);
        basketItem.setQuantity(1);

        User user = new User();
        user.setId(1L);
        user.setBasketList(new ArrayList<>());
        user.getBasketList().add(basketItem);

        BasketItem basketItem1 = new BasketItem();
        basketItem1.setId(2L);
        basketItem1.setArticle(article);
        basketItem1.setQuantity(2);

        User user2 = new User();
        user2.setId(1L);
        user2.setBasketList(new ArrayList<>());
        user2.getBasketList().add(basketItem1);

        when(userRepository.getById(1L)).thenReturn(user);
        when(articleRepository.getById(1L)).thenReturn(article);
        when(basketItemRepository.getByArticleAndQuantity(article, 1)).thenReturn(Optional.of(basketItem));
        when(userRepository.save(user2)).thenReturn(user);

        // Act
        boolean decreased = articleService.decreaseCountOfBasketItem(1L, user2);

        // Assert
        assertTrue(decreased);
        assertEquals(1, user.getBasketList().get(0).getQuantity());
    }
}