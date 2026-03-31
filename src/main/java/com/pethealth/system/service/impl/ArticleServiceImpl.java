package com.pethealth.system.service.impl;

import com.pethealth.system.entity.Article;
import com.pethealth.system.repository.ArticleRepository;
import com.pethealth.system.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getArticleList() {
        return articleRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Article getArticleById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            // 增加浏览量
            Article existingArticle = article.get();
            existingArticle.setViews(existingArticle.getViews() != null ? existingArticle.getViews() + 1 : 1);
            return articleRepository.save(existingArticle);
        }
        return null;
    }

    @Override
    public Article createArticle(Article article) {
        // 初始化默认值
        article.setViews(0);
        article.setLikes(0);
        article.setFavorites(0);
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long id, Article article) {
        Optional<Article> existingArticle = articleRepository.findById(id);
        if (existingArticle.isPresent()) {
            Article updatedArticle = existingArticle.get();
            updatedArticle.setTitle(article.getTitle());
            updatedArticle.setContent(article.getContent());
            updatedArticle.setAuthor(article.getAuthor());
            updatedArticle.setCover(article.getCover());
            return articleRepository.save(updatedArticle);
        }
        return null;
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public Article likeArticle(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            Article existingArticle = article.get();
            existingArticle.setLikes(existingArticle.getLikes() != null ? existingArticle.getLikes() + 1 : 1);
            return articleRepository.save(existingArticle);
        }
        return null;
    }

    @Override
    public Article favoriteArticle(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            Article existingArticle = article.get();
            existingArticle.setFavorites(existingArticle.getFavorites() != null ? existingArticle.getFavorites() + 1 : 1);
            return articleRepository.save(existingArticle);
        }
        return null;
    }
}
