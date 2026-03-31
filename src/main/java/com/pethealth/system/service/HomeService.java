package com.pethealth.system.service;

import com.pethealth.system.entity.Article;
import com.pethealth.system.entity.Pet;
import com.pethealth.system.entity.Slide;
import java.util.List;
import java.util.Map;

public interface HomeService {
    Map<String, Object> getHomeData(Long userId);
    List<Slide> getSlides();
    List<Article> getLatestArticles(int limit);
    List<Pet> getUserPets(Long userId);
}
