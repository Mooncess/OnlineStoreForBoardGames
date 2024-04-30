package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.BasketItem;
import ru.mooncess.onlinestore.entity.User;
import ru.mooncess.onlinestore.mappers.ArticleMapper;
import ru.mooncess.onlinestore.repository.ArticleRepository;
import ru.mooncess.onlinestore.repository.BasketItemRepository;
import ru.mooncess.onlinestore.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BasketItemRepository basketItemRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final ArticleMapper mapper = Mappers.getMapper(ArticleMapper.class);

    public Optional<Article> createArticle(ArticleCreateDTO articleCreateDTO, MultipartFile image) {
        try {
            String imageURN = imageService.addImage(articleCreateDTO.getName(), image);
            Article newArticle = mapper.articleCreateDtoToEntity(articleCreateDTO);
            newArticle.setImageURN(imageURN);
            return Optional.of(articleRepository.save(newArticle));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean addToBasket(Long articleId, User user) {
        try {
            boolean flag = isArticleInBasket(articleId, user);
            if(flag) { return true; }
            BasketItem basketItem = new BasketItem();
            basketItem.setArticle(articleRepository.getById(articleId));
            basketItem.setQuantity(1);
            Optional<BasketItem> optionalBasketItem = basketItemRepository.getByArticleAndQuantity(basketItem.getArticle(), basketItem.getQuantity());

            if(optionalBasketItem.isPresent()) {
                user.getBasketList().add(optionalBasketItem.get());
                userRepository.save(user);
                return true;
            }

            user.getBasketList().add(basketItemRepository.save(basketItem));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean increaseCountOfBasketItem(Long articleId, User user) {
        try {
            List<BasketItem> basketlist = user.getBasketList();
            Optional<BasketItem> existingBasketItem = basketlist.stream()
                    .filter(a -> a.getArticle().getId().equals(articleId))
                    .findFirst();

            if(existingBasketItem.isEmpty()) {
                return false;
            }

            BasketItem basketItem = new BasketItem();
            basketItem.setArticle(existingBasketItem.get().getArticle());
            basketItem.setQuantity(existingBasketItem.get().getQuantity() + 1);

            Optional<BasketItem> newBasketItem = basketItemRepository.getByArticleAndQuantity(basketItem.getArticle(), basketItem.getQuantity());

            if(newBasketItem.isEmpty()) {
                newBasketItem = Optional.of(basketItemRepository.save(basketItem));
            }

            user.getBasketList().remove(existingBasketItem.get());
            user.getBasketList().add(newBasketItem.get());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean decreaseCountOfBasketItem(Long articleId, User user) {
        try {
            List<BasketItem> basketlist = user.getBasketList();
            Optional<BasketItem> existingBasketItem = basketlist.stream()
                    .filter(a -> a.getArticle().getId().equals(articleId))
                    .findFirst();

            if(existingBasketItem.isEmpty()) {
                return false;
            }

            BasketItem basketItem = new BasketItem();
            basketItem.setArticle(existingBasketItem.get().getArticle());
            basketItem.setQuantity(existingBasketItem.get().getQuantity() - 1);

            if(basketItem.getQuantity() == 0) {
                return deleteArticleFromBasket(articleId, user);
            }

            Optional<BasketItem> newBasketItem = basketItemRepository.getByArticleAndQuantity(basketItem.getArticle(), basketItem.getQuantity());

            if(newBasketItem.isEmpty()) {
                newBasketItem = Optional.of(basketItemRepository.save(basketItem));
            }

            user.getBasketList().remove(existingBasketItem.get());
            user.getBasketList().add(newBasketItem.get());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isArticleInBasket(Long articleId, User user) {
        List<BasketItem> basketlist = user.getBasketList();
        return basketlist.stream()
                .anyMatch(a -> a.getId().equals(articleId));
    }

    public boolean addToWishlist(Long articleId, User user) {
        try {
            Optional<Article> optional = articleRepository.findById(articleId);
            if(optional.isPresent()) {
                Article article = optional.get();
                List<Article> wishlist = user.getWishList();

                boolean isArticleInWishlist = wishlist.stream()
                        .anyMatch(a -> a.equals(article));

                if (!isArticleInWishlist) {
                    wishlist.add(article);
                    user.setWishList(wishlist);
                    userRepository.save(user);
                }
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Article> updateArticle(Long id, ArticleCreateDTO article, MultipartFile image) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            try {
                if (!image.isEmpty()) {
                    imageService.updateImage(optionalArticle.get(), image);
                }
                Article updatedArticle = mapper.articleCreateDtoToEntity(article);
                updatedArticle.setId(id);
                updatedArticle.setImageURN(optionalArticle.get().getImageURN());
                return Optional.of(articleRepository.save(updatedArticle));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public boolean deleteArticle(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            imageService.deleteImage(optionalArticle.get().getImageURN());
            articleRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteArticleFromBasket(Long articleId, User user) {
        List<BasketItem> basketlist = user.getBasketList();
        Optional<BasketItem> existingBasketItem = basketlist.stream()
                .filter(a -> a.getArticle().getId().equals(articleId))
                .findFirst();
        if (existingBasketItem.isPresent()) {
            user.getBasketList().remove(existingBasketItem.get());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteArticleFromWishlist(Long articleId, User user) {
        List<Article> list = user.getWishList();
        Optional<Article> optional = list.stream()
                .filter(a -> a.getId().equals(articleId))
                .findFirst();
        if (optional.isPresent()) {
            user.getWishList().remove(optional.get());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public List<Article> getAllArticle() {
        return articleRepository.findAll();
    }
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findArticleByCategory(Long categoryId){
        return articleRepository.findArticleByCategory(categoryId);
    }

    public List<Article> findArticleByPriceAsc(){
        return articleRepository.findArticleByPriceAsc();
    }

    public List<Article> findArticleByCategoryAndSortByPriceAsc(Long categoryId){
        return articleRepository.findArticleByCategoryAndSortByPriceAsc(categoryId);
    }

    public List<Article> findArticleByPriceDesc(){
        return articleRepository.findArticleByPriceDesc();
    }

    public List<Article> findArticleByCategoryAndSortByPriceDesc(Long categoryId){
        return articleRepository.findArticleByCategoryAndSortByPriceDesc(categoryId);
    }

    public List<Article> findArticleByNameAsc(){
        return articleRepository.findArticleByNameAsc();
    }

    public List<Article> findArticleByCategoryAndSortByNameAsc(Long categoryId){
        return articleRepository.findArticleByCategoryAndSortByNameAsc(categoryId);
    }

    public List<Article> findArticleByNameDesc(){
        return articleRepository.findArticleByNameDesc();
    }

    public List<Article> findArticleByCategoryAndSortByNameDesc(Long categoryId){
        return articleRepository.findArticleByCategoryAndSortByNameDesc(categoryId);
    }
    public List<Article> findArticleByReservesAsc(){
        return articleRepository.findArticleByReservesAsc();
    }

    public List<Article> findArticleByReservesDesc(){
        return articleRepository.findArticleByReservesDesc();
    }
    public List<Article> findArticleBySimName(String reg) { return articleRepository.findArticleBySimName(reg); }
    public List<Article> findArticleBySimNameAndSortByPriceAsc(String reg) { return articleRepository.findArticleBySimNameAndSortByPriceAsc(reg); }
    public List<Article> findArticleBySimNameAndSortByPriceDesc(String reg) { return articleRepository.findArticleBySimNameAndSortByPriceDesc(reg); }
    public List<Article> findArticleBySimNameAndSortByNameAsc(String reg) { return articleRepository.findArticleBySimNameAndSortByNameAsc(reg); }
    public List<Article> findArticleBySimNameAndSortByNameDesc(String reg) { return articleRepository.findArticleBySimNameAndSortByNameDesc(reg); }
    public List<Article> findArticleByCategoryAndSimName(Long categoryId, String reg) { return articleRepository.findArticleByCategoryAndSimName(categoryId, reg); }
    public List<Article> findArticleByCategoryAndSimNameAndSortByPriceAsc(Long categoryId, String reg) { return articleRepository.findArticleByCategoryAndSimNameAndSortByPriceAsc(categoryId, reg); }
    public List<Article> findArticleByCategoryAndSimNameAndSortByPriceDesc(Long categoryId, String reg) { return articleRepository.findArticleByCategoryAndSimNameAndSortByPriceDesc(categoryId, reg); }
    public List<Article> findArticleByCategoryAndSimNameAndSortByNameAsc(Long categoryId, String reg) { return articleRepository.findArticleByCategoryAndSimNameAndSortByNameAsc(categoryId, reg); }
    public List<Article> findArticleByCategoryAndSimNameAndSortByNameDesc(Long categoryId, String reg) { return articleRepository.findArticleByCategoryAndSimNameAndSortByNameDesc(categoryId, reg); }
}