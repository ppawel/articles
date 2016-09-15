package com.ppawel.articles.repository;

import com.ppawel.articles.model.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * API for accessing and managing {@link Article}s.
 */
public interface ArticleRepository extends CrudRepository<Article, Long> {

    /**
     * Lists articles by given author using exact matching against the {@link Article#authors} collection.
     * <p>
     * Note: This method is named according to Spring Data convention allowing auto-generating implementation.
     *
     * @param author author to match
     * @return matching articles
     */
    List<Article> findByAuthors(String author);

    /**
     * Finds articles by using simple wildcard matching on article text fields and also exact matching against
     * the {@link Article#keywords} collection.
     *
     * @param keyword keyword to use for search
     * @return matching articles
     */
    @Query("SELECT a FROM Article a WHERE " +
            "a.header LIKE CONCAT('%', :keyword, '%') OR " +
            "a.description LIKE CONCAT('%', :keyword, '%') OR " +
            "a.content LIKE CONCAT('%', :keyword, '%') OR " +
            "a.header LIKE CONCAT('%', :keyword, '%') OR " +
            ":keyword MEMBER OF a.keywords"
    )
    List<Article> search(@Param("keyword") String keyword);
}
