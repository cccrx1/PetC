package com.pethealth.system.service;

import com.pethealth.system.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> getArticleList();
    Article getArticleById(Long id);
    Article createArticle(Article article);
    Article updateArticle(Long id, Article article);
    void deleteArticle(Long id);
    Article likeArticle(Long id);
    Article favoriteArticle(Long id);
}
