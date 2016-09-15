package com.ppawel.articles;

import com.ppawel.articles.model.Article;
import com.ppawel.articles.repository.ArticleRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for the data access layer - {@link ArticleRepository} - CRUD operations.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataAccessTestContext.class)
public class ArticleRepositoryCrudTests {

    @Autowired
    private ArticleRepository repository;

    @After
    public void before() {
        repository.deleteAll();
    }

    @Test
    public void testCreateEmpty() {
        Article article = new Article();
        article = repository.save(article);
        assertThat(article.getId(), notNullValue());
    }

    @Test
    public void testCreateWithAuthors() {
        Article article = new Article();
        article.addAuthors("author1", "author2", "some other author");

        article = repository.save(article);

        assertThat(article.getId(), notNullValue());
        assertThat(article.getAuthors(), hasSize(3));
        assertThat(article.getAuthors(), hasItems("author1", "author2", "some other author"));
    }

    @Test
    public void testCreateWithKeywords() {
        Article article = new Article();
        article.addKeywords("cats", "dogs");

        article = repository.save(article);

        assertThat(article.getId(), notNullValue());
        assertThat(article.getKeywords(), hasSize(2));
        assertThat(article.getKeywords(), hasItems("cats", "dogs"));
    }

    @Test
    public void testUpdateAndFind() {
        Article article = new Article();
        article.addKeywords("cats", "dogs");
        article.addAuthors("author1", "author2", "some other author");

        repository.save(article);

        article = repository.findOne(article.getId());

        // Update and save again

        article.setHeader("title");
        article.setContent("content here");
        article.setDescription("short");

        article.getAuthors().remove("author1");
        article.getAuthors().remove("author2");
        article.addAuthors("new author");

        article.getKeywords().remove("cats");
        article.getKeywords().remove("dogs");
        article.getKeywords().add("news");

        repository.save(article);

        // Find again in the repository

        Article updated = repository.findOne(article.getId());

        assertEquals(updated.getId(), article.getId());

        assertThat(updated.getHeader(), is("title"));
        assertThat(updated.getContent(), is("content here"));
        assertThat(updated.getDescription(), is("short"));

        assertThat(updated.getAuthors(), hasSize(2));
        assertThat(updated.getAuthors(), hasItems("new author", "some other author"));

        assertThat(updated.getKeywords(), hasSize(1));
        assertThat(updated.getKeywords(), hasItems("news"));
    }

    @Test
    public void testDelete() {
        // Save

        Article article = new Article();
        article.addKeywords("cats", "dogs");
        article = repository.save(article);

        // Delete

        repository.delete(article.getId());

        // Try to retrieve

        article = repository.findOne(article.getId());

        assertThat(article, nullValue());
    }
}
