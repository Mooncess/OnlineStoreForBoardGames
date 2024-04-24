package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.exception.AppError;
import ru.mooncess.onlinestore.mappers.ArticleMapper;
import ru.mooncess.onlinestore.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper mapper = Mappers.getMapper(ArticleMapper.class);

    // Create
    public Optional<Article> createArticle(ArticleCreateDTO articleCreateDTO, String imageURN) {
        try {
            Article newArticle = mapper.articleCreateDtoToEntity(articleCreateDTO);
            newArticle.setImageURN(imageURN);
            return Optional.of(articleRepository.save(newArticle));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
