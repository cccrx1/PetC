package com.pethealth.system.service.impl;

import com.pethealth.system.entity.Slide;
import com.pethealth.system.repository.SlideRepository;
import com.pethealth.system.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SlideServiceImpl implements SlideService {
    
    @Autowired
    private SlideRepository slideRepository;
    
    @Override
    public List<Slide> getActiveSlides() {
        return slideRepository.findByActiveTrueOrderByOrderIndexAsc();
    }
}
