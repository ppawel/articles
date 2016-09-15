package com.ppawel.articles.rest;

import com.ppawel.articles.model.Article;
import com.ppawel.articles.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for articles, uses data access layer directly but usually it would go through service->data access
 * or even something like facade->service->data access layers instead.
 */
@RestController
public class ArticleResource {

    @Autowired
    private ArticleRepository repository;

    /**
     * Returns a single article with given id.
     */
    @RequestMapping(path = "/api/articles/{id}", method = RequestMethod.GET)
    public Article getArticle(@PathVariable Long id) {
        return repository.findOne(id);
    }

    /**
     * Deletes a single article with given id.
     */
    @RequestMapping(path = "/api/articles/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteArticle(@PathVariable Long id) {
        // Check if exists
        if (!repository.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        // Delete
        repository.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates a single article with given id and returns updated instance or 404 when id not found.
     */
    @RequestMapping(path = "/api/articles/{id}", method = RequestMethod.POST)
    public ResponseEntity updateArticle(@RequestBody Article article) {
        // Check if exists
        if (!repository.exists(article.getId())) {
            return ResponseEntity.notFound().build();
        }

        // Update
        Article updated = repository.save(article);
        return ResponseEntity.ok().body(updated);
    }

    /**
     * Creates new article and returns newly created instance.
     */
    @RequestMapping(path = "/api/articles", method = RequestMethod.PUT)
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article created = repository.save(article);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    /**
     * List articles by author or by period, depending on query parameters.
     */
    @RequestMapping(path = "/api/articles", method = RequestMethod.GET)
    public List<Article> listArticles(@RequestParam(name = "author", required = false) String author) {
        return repository.findByAuthors(author);
    }

    /**
     * Finds articles with given keyword.
     */
    @RequestMapping(path = "/api/search", method = RequestMethod.GET)
    public List<Article> search(@RequestParam String keyword) {
        return repository.search(keyword);
    }
}
