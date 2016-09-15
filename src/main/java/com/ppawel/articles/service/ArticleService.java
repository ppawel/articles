package com.ppawel.articles.service;

import com.ppawel.articles.model.Article;
import com.ppawel.articles.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service layer component responsible for accessing data, validating input semantics, throwing business exceptions.
 * Also includes security attributes using Spring Security global method security.
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository repository;

    /**
     * Creates new article with given input.
     *
     * @param article input to use
     * @return created instance
     * @throws IllegalArgumentException when id is filled in in the input
     */
    @Secured("ROLE_EDITOR")
    public Article create(Article article) {
        if (article.getId() != null) {
            throw new IllegalArgumentException("Article to be created cannot contain id");
        }
        return repository.save(article);
    }

    /**
     * Updates an existing article with given input.
     *
     * @param article input to use
     * @return updated instance
     * @throws ArticleNotFoundException when article with id given in input is not found
     */
    @Secured("ROLE_EDITOR")
    public Article update(Article article) throws ArticleNotFoundException {
        // Check if exists
        if (!repository.exists(article.getId())) {
            throw new ArticleNotFoundException();
        }
        // Update
        return repository.save(article);
    }

    /**
     * Deletes an existing article with given id.
     *
     * @param id id to use
     * @throws ArticleNotFoundException when article with given id not found
     */
    @Secured("ROLE_EDITOR")
    public void delete(Long id) throws ArticleNotFoundException {
        // Check if exists
        if (!repository.exists(id)) {
            throw new ArticleNotFoundException();
        }

        // Delete
        repository.delete(id);
    }

    /**
     * Retrieves article with given id.
     *
     * @param id id to use
     * @return found article
     * @throws ArticleNotFoundException when article with given id not found
     */
    public Article get(Long id) throws ArticleNotFoundException {
        Article article = repository.findOne(id);

        if (article == null) {
            throw new ArticleNotFoundException();
        }

        return article;
    }

    /**
     * Finds articles by given criteria. If author is given then it takes precedence over from/to parameters.
     * Otherwise from/to must both be defined.
     *
     * @param author author to use
     * @param from   from date to use
     * @param to     from date to use
     * @return list of matching articles
     */
    public List<Article> find(String author, Date from, Date to) {
        if (author != null) {
            return repository.findByAuthors(author);
        } else {
            return repository.findByDatePublishedBetween(from, to);
        }
    }

    /**
     * Finds articles by specified keyword.
     *
     * @param keyword keyword to use
     * @return list of matching articles
     */
    public List<Article> search(String keyword) {
        return repository.search(keyword);
    }
}
