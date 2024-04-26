package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mooncess.onlinestore.dto.ArticleCreateDTO;
import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.mappers.ArticleMapper;
import ru.mooncess.onlinestore.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final ArticleMapper mapper = Mappers.getMapper(ArticleMapper.class);

    // Create
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
    public List<Article> findArticleSimName(String reg) {
        return articleRepository.findArticleSimName(reg);
    }
}
