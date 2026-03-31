package com.pethealth.system.controller;

import com.pethealth.system.entity.User;
import com.pethealth.system.repository.UserRepository;
import com.pethealth.system.service.Text2SQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/text2sql")
public class Text2SQLController {

    @Autowired
    private Text2SQLService text2SQLService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/query")
    public String executeQuery(@RequestBody String naturalLanguage, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();
        
        return text2SQLService.executeQuery(naturalLanguage, userId);
    }
}