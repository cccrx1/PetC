package com.pethealth.system.controller;

import com.pethealth.system.entity.User;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        logger.info("Registering user: {}", user.getUsername());
        logger.info("User email: {}", user.getEmail());
        logger.info("User name: {}", user.getName());
        logger.info("User password: {}", user.getPassword());
        try {
            // 检查用户名是否为null
            if (user.getUsername() == null) {
                logger.error("Username is null");
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Username is null");
                return ResponseEntity.status(400).body(response);
            }
            
            // 检查邮箱是否为null
            if (user.getEmail() == null) {
                logger.error("Email is null");
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Email is null");
                return ResponseEntity.status(400).body(response);
            }
            
            // 检查密码是否为null
            if (user.getPassword() == null) {
                logger.error("Password is null");
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Password is null");
                return ResponseEntity.status(400).body(response);
            }
            
            User registeredUser = userService.register(user);
            logger.info("User registered successfully: {}", user.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("user", registeredUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            logger.error("Error stack trace: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String usernameOrEmail = credentials.get("username");
        String password = credentials.get("password");
        try {
            String token = userService.login(usernameOrEmail, password);
            // 从token中提取用户名
            String username = jwtUtil.extractUsername(token);
            User user = userService.getProfile(username);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for user: {}", usernameOrEmail, e);
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        // 从SecurityContextHolder中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        User user = userService.getProfile(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser) {
        // 从SecurityContextHolder中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        User user = userService.updateProfile(username, updatedUser);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody Map<String, String> passwordData, HttpServletRequest request) {
        logger.info("Received update password request");
        try {
            // 从请求头中获取token
            String authorizationHeader = request.getHeader("Authorization");
            logger.info("Authorization header: {}", authorizationHeader);
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.error("Authorization header is null or invalid");
                return ResponseEntity.status(401).build();
            }
            
            String jwt = authorizationHeader.substring(7);
            logger.info("Extracted JWT: {}", jwt);
            String username = jwtUtil.extractUsername(jwt);
            logger.info("Updating password for user: {}", username);
            
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            logger.info("Current password: {}, New password: {}", currentPassword, newPassword);
            
            if (currentPassword == null || newPassword == null) {
                logger.error("Current password or new password is null");
                return ResponseEntity.status(400).build();
            }
            
            // 验证当前密码
            logger.info("Authenticating user: {}", username);
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, currentPassword)
                );
                logger.info("Authentication successful for user: {}", username);
            } catch (Exception e) {
                logger.error("Authentication failed for user: {}", username, e);
                return ResponseEntity.status(401).build();
            }
            
            // 更新密码
            logger.info("Updating password for user: {}", username);
            try {
                userService.updatePassword(username, newPassword);
                logger.info("Password updated successfully for user: {}", username);
            } catch (Exception e) {
                logger.error("Error updating password for user: {}", username, e);
                return ResponseEntity.status(500).build();
            }
            
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error updating password: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("Test endpoint called");
        return ResponseEntity.ok("Test successful");
    }
}