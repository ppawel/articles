package com.ppawel.articles.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Represents an article.
 * <p>
 * Note: For simplicity and keeping focus on task's main priorities authors and keywords
 * are modeled as basic collections of strings, as opposed to separate model classes.
 */
@Entity
public class Article {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String header;

    @Column
    private String description;

    @Column
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date datePublished;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> authors = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> keywords = new ArrayList<>();

    public void addAuthors(String... authors) {
        this.authors.addAll(Arrays.asList(authors));
    }

    public void addKeywords(String... keywords) {
        this.keywords.addAll(Arrays.asList(keywords));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return id != null ? id.equals(article.id) : article.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
