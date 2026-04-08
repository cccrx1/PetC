package com.pethealth.system.controller;

import com.pethealth.system.entity.User;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        logger.info("Registering user: {}", user.getUsername());
        try {
            User registeredUser = userService.register(user);
            logger.info("User registered successfully: {}", user.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        String token = userService.login(username, password);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String token) {
        // 从token中提取用户名
        String jwt = token.substring(7); // 去掉Bearer前缀
        String username = jwtUtil.extractUsername(jwt);
        User user = userService.getProfile(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        // 从token中提取用户名
        String jwt = token.substring(7); // 去掉Bearer前缀
        String username = jwtUtil.extractUsername(jwt);
        User user = userService.updateProfile(username, updatedUser);
        return ResponseEntity.ok(user);
    }
}