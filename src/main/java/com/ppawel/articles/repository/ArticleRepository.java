package com.ppawel.articles.repository;

import com.ppawel.articles.model.Article;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ppawel on 15.09.16.
 */
public interface ArticleRepository extends CrudRepository<Article, Long> {
}
