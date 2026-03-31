package com.pethealth.system.controller;

import com.pethealth.system.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    
    @Autowired
    private HomeService homeService;
    
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getHomeData(@RequestParam Long userId) {
        Map<String, Object> homeData = homeService.getHomeData(userId);
        return new ResponseEntity<>(homeData, HttpStatus.OK);
    }
}
