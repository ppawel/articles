package com.ppawel.articles;

import com.ppawel.articles.model.Article;
import com.ppawel.articles.repository.ArticleRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for the data access layer - {@link ArticleRepository} - search operations.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataAccessTestContext.class)
public class ArticleRepositorySearchTests {

    @Autowired
    private ArticleRepository repository;

    @Before
    public void before() {
        repository.deleteAll();
    }

    @Test
    public void testFindByAuthor() {
        createArticles("author1", "a", 12);
        createArticles("author2", "b", 1);
        createArticles("some other one", "c", 7);

        List<Article> result = repository.findByAuthors("author1");
        assertThat(result, hasSize(12));
        assertThat(result, everyItem(hasProperty("authors", hasItem("author1"))));

        result = repository.findByAuthors("some other");
        assertThat(result, hasSize(0));

        result = repository.findByAuthors("some other one");
        assertThat(result, hasSize(7));
        assertThat(result, everyItem(hasProperty("authors", hasItem("some other one"))));

        result = repository.findByAuthors("author2");
        assertThat(result, hasSize(1));
        assertThat(result, everyItem(hasProperty("authors", hasItem("author2"))));
    }

    @Test
    public void testFindByPeriod() {
        Date now = new Date();
        createArticles(now, 12);
        createArticles(DateUtils.addMonths(now, 3), 1);
        createArticles(DateUtils.addMonths(now, 6), 7);

        List<Article> result = repository.findByDatePublishedBetween(now, DateUtils.addMonths(now, 12));
        assertThat(result, hasSize(20));

        result = repository.findByDatePublishedBetween(now, DateUtils.addMonths(now, 5));
        assertThat(result, hasSize(13));

        result = repository.findByDatePublishedBetween(DateUtils.addMonths(now, -15), DateUtils.addMonths(now, -5));
        assertThat(result, hasSize(0));
    }

    @Test
    public void testSearchByKeywordOnHeader() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        // Every test article has "article" in the header
        List<Article> result = repository.search("article");
        assertThat(result, hasSize(20));
    }

    @Test
    public void testSearchByKeywordOnContent() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        // Every test article has "content<keyword>" in the content
        List<Article> result = repository.search("contentccc");
        assertThat(result, hasSize(7));
    }

    @Test
    public void testSearchByKeywordOnDescription() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "aaa", 1);
        createArticles("author2", "bbb", 10);

        // Every test article has "short<keyword>" in the content
        List<Article> result = repository.search("contentaaa");
        assertThat(result, hasSize(13));
    }

    @Test
    public void testSearchByKeywordOnlyKeyword() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        List<Article> result = repository.search("bbb");
        assertThat(result, hasSize(1));
        assertThat(result, everyItem(hasProperty("keywords", hasItem("bbb"))));

        result = repository.search("ccc");
        assertThat(result, hasSize(7));
        assertThat(result, everyItem(hasProperty("keywords", hasItem("ccc"))));

        result = repository.search("ddd");
        assertThat(result, hasSize(0));
    }

    @Test
    public void testSearchByKeywordNoMatch() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        List<Article> result = repository.search("abcdef");
        assertThat(result, hasSize(0));
    }

    @Test
    public void testSearchByKeywordEmptyQuery() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        List<Article> result = repository.search("");
        assertThat(result, hasSize(20));
    }

    // Helper methods

    protected void createArticles(String author, String keyword, int count) {
        for (int i = 0; i < count; i++) {
            Article article = new Article();
            article.setDatePublished(new Date());
            article.setHeader("article" + i);
            article.setContent("some content" + keyword);
            article.addAuthors(author, "some other one " + count);
            article.addKeywords(keyword, "other" + count);
            repository.save(article);
        }
    }

    protected void createArticles(Date date, int count) {
        for (int i = 0; i < count; i++) {
            Article article = new Article();
            article.setHeader("article" + i);
            article.setDatePublished(date);
            repository.save(article);
        }
    }
}
