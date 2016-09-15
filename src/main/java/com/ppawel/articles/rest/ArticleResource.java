package com.ppawel.articles.rest;

import com.ppawel.articles.model.Article;
import com.ppawel.articles.service.ArticleNotFoundException;
import com.ppawel.articles.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for articles, uses service layer to perform CRUD and search operations.
 */
@RestController
public class ArticleResource {

    @Autowired
    private ArticleService service;

    /**
     * Return HTTP status 404 when {@link ArticleNotFoundException} occurs.
     */
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity articleNotFound() {
        return ResponseEntity.notFound().build();
    }

    /**
     * Returns a single article with given id.
     */
    @RequestMapping(path = "/api/articles/{id}", method = RequestMethod.GET)
    public Article getArticle(@PathVariable Long id) throws ArticleNotFoundException {
        return service.get(id);
    }

    /**
     * Deletes a single article with given id.
     */
    @RequestMapping(path = "/api/articles/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteArticle(@PathVariable Long id) throws ArticleNotFoundException {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates a single article with given id and returns updated instance or 404 when id not found.
     */
    @RequestMapping(path = "/api/articles/{id}", method = RequestMethod.POST)
    public ResponseEntity updateArticle(@RequestBody Article article) throws ArticleNotFoundException {
        Article updated = service.update(article);
        return ResponseEntity.ok().body(updated);
    }

    /**
     * Creates new article and returns newly created instance.
     */
    @RequestMapping(path = "/api/articles", method = RequestMethod.PUT)
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article created = service.create(article);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    /**
     * List articles by author or by period, depending on query parameters.
     */
    @RequestMapping(path = "/api/articles", method = RequestMethod.GET)
    public List<Article> listArticles(@RequestParam(name = "author", required = false) String author) {
        return service.findByAuthors(author);
    }

    /**
     * Finds articles with given keyword.
     */
    @RequestMapping(path = "/api/search", method = RequestMethod.GET)
    public List<Article> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}
