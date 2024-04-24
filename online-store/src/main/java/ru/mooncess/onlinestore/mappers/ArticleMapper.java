package ru.mooncess.onlinestore.mappers;

import org.mapstruct.Mapper;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.Category;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ArticleMapper {
    default Article articleCreateDtoToEntity(ArticleCreateDTO dto) {
        Article article = new Article();
        article.setDescription(dto.getDescription());
        article.setQuantityOfPlayers(dto.getQuantityOfPlayers());
        article.setGameTime(dto.getGameTime());
        article.setYearOfRelease(dto.getYearOfRelease());
        article.setProducer(dto.getProducer());
        article.setRecommendedAge(dto.getRecommendedAge());
        article.setOldPrice(dto.getOldPrice());
        article.setActualPrice(dto.getActualPrice());
        article.setReserves(dto.getReserves());

        // Маппинг свойства Category
        List<Category> categories = new ArrayList<>();
        List<Long> list = dto.getCategory();
        for (long i : list) {
            Category category = new Category();
            category.setId(i);
            categories.add(category);
        }
        article.setCategory(categories);

        return article;
    }
}