package com.ppawel.articles;

import com.ppawel.articles.model.Article;
import com.ppawel.articles.repository.ArticleRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Tests for REST API using real servlet environment, not a mock one. Spring Boot brings up embedded Tomcat
 * on a random port with the application context and we are able to run RestAssured tests against it
 * while at the same time have access to the context itself from the test - how awesome is that?!
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiTests {

    // Random port injected here
    @LocalServerPort
    private int serverPort;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ArticleRepository repository;

    @Before
    public void before() {
        RestAssured.port = serverPort;
        enableAuthentication();
        repository.deleteAll();
    }

    @Test
    public void testListByAuthor() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        disableAuthentication();
        given().param("author", "some other one")
                .get("/api/articles")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(7));
    }


    @Test
    public void testListByPeriod() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other one", "ccc", 7);

        disableAuthentication();

        // All articles created just now
        given().param("from", System.currentTimeMillis() - 1000 * 60).param("to", System.currentTimeMillis() + 1000 * 60)
                .get("/api/articles")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(20));

        // Period from the past - should be no matching
        given().param("from", System.currentTimeMillis() - 1000 * 60).param("to", System.currentTimeMillis() - 1000 * 30)
                .get("/api/articles")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(0));
    }

    @Test
    public void testSearch() {
        createArticles("author1", "aaa", 12);
        createArticles("author2", "bbb", 1);
        createArticles("some other", "aaa", 7);

        disableAuthentication();
        given().param("keyword", "aaa")
                .get("/api/search")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(19));
    }

    @Test
    public void testDelete404() {
        given().delete("/api/articles/123").then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testUpdate404() {
        given().delete("/api/articles/123").then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGet404() {
        given().get("/api/articles/123").then().statusCode(HttpStatus.NOT_FOUND.value());

        // Again with disabled login
        disableAuthentication();
        given().get("/api/articles/123").then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDelete401() {
        disableAuthentication();
        given().delete("/api/articles/123").then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testUpdate401() {
        disableAuthentication();
        given().post("/api/articles/123").then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testCreate401() {
        disableAuthentication();
        given().put("/api/articles").then().statusCode(HttpStatus.UNAUTHORIZED.value());
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

            given()
                    .body(article).contentType(ContentType.JSON)
                    .put("/api/articles")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }
    }

    /**
     * Sets HTTP Basic auth for all following RestAssured requests.
     */
    protected void enableAuthentication() {
        RestAssured.authentication = basic(securityProperties.getUser().getName(), securityProperties.getUser().getPassword());
    }

    /**
     * Disables auth for RestAssured requests.
     */
    protected void disableAuthentication() {
        RestAssured.authentication = DEFAULT_AUTH;
    }
}
