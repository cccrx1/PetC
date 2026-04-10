package com.pethealth.system.controller;

import com.pethealth.system.dto.ResponseDTO;
import com.pethealth.system.entity.User;
import com.pethealth.system.exception.BusinessException;
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
    public ResponseEntity<ResponseDTO<?>> register(@RequestBody User user) {
        logger.info("Registering user: {}", user.getUsername());
        try {
            // 检查参数
            if (user.getUsername() == null) {
                throw new BusinessException("Username is required");
            }
            if (user.getEmail() == null) {
                throw new BusinessException("Email is required");
            }
            if (user.getPassword() == null) {
                throw new BusinessException("Password is required");
            }
            
            User registeredUser = userService.register(user);
            logger.info("User registered successfully: {}", user.getUsername());
            return ResponseEntity.ok(ResponseDTO.success(registeredUser));
        } catch (BusinessException e) {
            logger.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.status(e.getCode()).body(ResponseDTO.error(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            logger.error("Error stack trace: ", e);
            return ResponseEntity.ok(ResponseDTO.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<?>> login(@RequestBody Map<String, String> credentials) {
        String usernameOrEmail = credentials.get("username");
        String password = credentials.get("password");
        try {
            if (usernameOrEmail == null || password == null) {
                throw new BusinessException("Username and password are required");
            }
            
            String token = userService.login(usernameOrEmail, password);
            // 从token中提取用户名
            String username = jwtUtil.extractUsername(token);
            User user = userService.getProfile(username);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", user);
            
            return ResponseEntity.ok(ResponseDTO.success(responseData));
        } catch (BusinessException e) {
            logger.error("Login failed for user: {}", usernameOrEmail, e);
            return ResponseEntity.ok(ResponseDTO.error(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error("Login failed for user: {}", usernameOrEmail, e);
            return ResponseEntity.status(401).body(ResponseDTO.error(401, "Invalid username or password"));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO<?>> getProfile() {
        // 从SecurityContextHolder中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.ok(ResponseDTO.unauthorized());
        }
        
        String username = authentication.getName();
        try {
            User user = userService.getProfile(username);
            return ResponseEntity.ok(ResponseDTO.success(user));
        } catch (Exception e) {
            logger.error("Error getting profile: {}", e.getMessage());
            return ResponseEntity.ok(ResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseDTO<?>> updateProfile(@RequestBody User updatedUser) {
        // 从SecurityContextHolder中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.ok(ResponseDTO.unauthorized());
        }
        
        String username = authentication.getName();
        try {
            User user = userService.updateProfile(username, updatedUser);
            return ResponseEntity.ok(ResponseDTO.success(user));
        } catch (Exception e) {
            logger.error("Error updating profile: {}", e.getMessage());
            return ResponseEntity.ok(ResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<ResponseDTO<?>> updatePassword(@RequestBody Map<String, String> passwordData, HttpServletRequest request) {
        logger.info("Received update password request");
        try {
            // 检查request是否为null
            if (request == null) {
                return ResponseEntity.ok(ResponseDTO.unauthorized());
            }
            
            // 从请求头中获取token
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(ResponseDTO.unauthorized());
            }
            
            String jwt = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            logger.info("Updating password for user: {}", username);
            
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (currentPassword == null || newPassword == null) {
                throw new BusinessException("Current password and new password are required");
            }
            
            // 验证当前密码
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, currentPassword)
                );
                logger.info("Authentication successful for user: {}", username);
            } catch (Exception e) {
                logger.error("Authentication failed for user: {}", username, e);
                return ResponseEntity.status(401).body(ResponseDTO.error(401, "Current password is incorrect"));
            }
            
            // 更新密码
            userService.updatePassword(username, newPassword);
            logger.info("Password updated successfully for user: {}", username);
            
            return ResponseEntity.ok(ResponseDTO.success());
        } catch (BusinessException e) {
            logger.error("Error updating password: {}", e.getMessage());
            return ResponseEntity.status(e.getCode()).body(ResponseDTO.error(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating password: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.serverError());
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<ResponseDTO<?>> test() {
        logger.info("Test endpoint called");
        return ResponseEntity.ok(ResponseDTO.success("Test successful"));
    }
}