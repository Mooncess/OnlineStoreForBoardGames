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

    @Query("SELECT a FROM Article a WHERE a.name LIKE %:regexPattern%")
    List<Article> findArticleBySimName(@Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a WHERE a.name LIKE %:regexPattern% ORDER BY a.actualPrice ASC")
    List<Article> findArticleBySimNameAndSortByPriceAsc(@Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a WHERE a.name LIKE %:regexPattern% ORDER BY a.actualPrice DESC")
    List<Article> findArticleBySimNameAndSortByPriceDesc(@Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a WHERE a.name LIKE %:regexPattern% ORDER BY a.name ASC")
    List<Article> findArticleBySimNameAndSortByNameAsc(@Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a WHERE a.name LIKE %:regexPattern% ORDER BY a.name DESC")
    List<Article> findArticleBySimNameAndSortByNameDesc(@Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE a.name LIKE %:regexPattern% AND ac.id = :categoryId")
    List<Article> findArticleByCategoryAndSimName(@Param("categoryId") Long categoryId, @Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE a.name LIKE %:regexPattern% AND ac.id = :categoryId ORDER BY a.actualPrice ASC")
    List<Article> findArticleByCategoryAndSimNameAndSortByPriceAsc(@Param("categoryId") Long categoryId, @Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE a.name LIKE %:regexPattern% AND ac.id = :categoryId ORDER BY a.actualPrice DESC")
    List<Article> findArticleByCategoryAndSimNameAndSortByPriceDesc(@Param("categoryId") Long categoryId, @Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE a.name LIKE %:regexPattern% AND ac.id = :categoryId ORDER BY a.name ASC")
    List<Article> findArticleByCategoryAndSimNameAndSortByNameAsc(@Param("categoryId") Long categoryId, @Param("regexPattern") String regexPattern);

    @Query("SELECT a FROM Article a JOIN a.category ac WHERE a.name LIKE %:regexPattern% AND ac.id = :categoryId ORDER BY a.name DESC")
    List<Article> findArticleByCategoryAndSimNameAndSortByNameDesc(@Param("categoryId") Long categoryId, @Param("regexPattern") String regexPattern);
}