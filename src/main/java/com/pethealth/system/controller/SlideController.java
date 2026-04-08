package com.pethealth.system.controller;

import com.pethealth.system.entity.Slide;
import com.pethealth.system.service.SlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/slides")
public class SlideController {
    
    @Autowired
    private SlideService slideService;
    
    @GetMapping
    public ResponseEntity<List<Slide>> getSlides() {
        List<Slide> slides = slideService.getActiveSlides();
        return new ResponseEntity<>(slides, HttpStatus.OK);
    }
}
