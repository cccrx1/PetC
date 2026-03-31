package com.pethealth.system.service.impl;

import com.pethealth.system.entity.Article;
import com.pethealth.system.entity.Pet;
import com.pethealth.system.entity.Slide;
import com.pethealth.system.repository.ArticleRepository;
import com.pethealth.system.repository.PetRepository;
import com.pethealth.system.service.HomeService;
import com.pethealth.system.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeServiceImpl implements HomeService {
    
    @Autowired
    private SlideService slideService;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    @Override
    public Map<String, Object> getHomeData(Long userId) {
        Map<String, Object> homeData = new HashMap<>();
        
        // 获取轮播图数据
        List<Slide> slides = getSlides();
        homeData.put("slides", slides);
        
        // 获取最新文章
        List<Article> articles = getLatestArticles(5);
        homeData.put("articles", articles);
        
        // 获取用户宠物
        List<Pet> pets = getUserPets(userId);
        homeData.put("pets", pets);
        
        // 可以添加其他首页数据，如未处理的提醒等
        
        return homeData;
    }
    
    @Override
    public List<Slide> getSlides() {
        return slideService.getActiveSlides();
    }
    
    @Override
    public List<Article> getLatestArticles(int limit) {
        List<Article> allArticles = articleRepository.findAllByOrderByCreatedAtDesc();
        return allArticles.stream().limit(limit).toList();
    }
    
    @Override
    public List<Pet> getUserPets(Long userId) {
        return petRepository.findByUserId(userId);
    }
}
