package ru.mooncess.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mooncess.onlinestore.entity.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a JOIN a.category ac WHERE ac.id = :categoryId")
    List<Article> findArticleByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Article a ORDER BY a.actualPrice ASC")
    List<Article> findArticleByPriceAsc();

    @Query("SELECT a FROM Article a ORDER BY a.actualPrice DESC")
    List<Article> findArticleByPriceDesc();

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE ac.id = :categoryId ORDER BY a.actualPrice ASC")
    List<Article> findArticleByCategoryAndSortByPriceAsc(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE ac.id = :categoryId ORDER BY a.actualPrice DESC")
    List<Article> findArticleByCategoryAndSortByPriceDesc(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Article a ORDER BY a.name ASC")
    List<Article> findArticleByNameAsc();

    @Query("SELECT a FROM Article a ORDER BY a.name DESC")
    List<Article> findArticleByNameDesc();

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE ac.id = :categoryId ORDER BY a.name ASC")
    List<Article> findArticleByCategoryAndSortByNameAsc(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE ac.id = :categoryId ORDER BY a.name DESC")
    List<Article> findArticleByCategoryAndSortByNameDesc(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Article a ORDER BY a.reserves ASC")
    List<Article> findArticleByReservesAsc();

    @Query("SELECT a FROM Article a ORDER BY a.reserves DESC")
    List<Article> findArticleByReservesDesc();
}