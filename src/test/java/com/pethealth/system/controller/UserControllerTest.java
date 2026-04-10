package com.pethealth.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pethealth.system.dto.ResponseDTO;
import com.pethealth.system.entity.User;
import com.pethealth.system.exception.BusinessException;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRegister_Success() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");

        // 模拟行为
        when(userService.register(user)).thenReturn(user);

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.register(user);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(user, response.getBody().getData());
        verify(userService, times(1)).register(user);
    }

    @Test
    void testRegister_UsernameNull() {
        // 准备测试数据
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.register(user);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Username is required", response.getBody().getMessage());
        verify(userService, never()).register(any());
    }

    @Test
    void testRegister_EmailNull() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setName("Test User");

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.register(user);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Email is required", response.getBody().getMessage());
        verify(userService, never()).register(any());
    }

    @Test
    void testRegister_PasswordNull() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setName("Test User");

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.register(user);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Password is required", response.getBody().getMessage());
        verify(userService, never()).register(any());
    }

    @Test
    void testRegister_BusinessException() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");

        // 模拟行为
        when(userService.register(user)).thenThrow(new BusinessException("Registration failed"));

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.register(user);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Registration failed", response.getBody().getMessage());
        verify(userService, times(1)).register(user);
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "password123");

        User user = new User();
        user.setUsername("testuser");
        user.setName("Test User");

        // 模拟行为
        when(userService.login("testuser", "password123")).thenReturn("test-token");
        when(jwtUtil.extractUsername("test-token")).thenReturn("testuser");
        when(userService.getProfile("testuser")).thenReturn(user);

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.login(credentials);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(userService, times(1)).login("testuser", "password123");
        verify(jwtUtil, times(1)).extractUsername("test-token");
        verify(userService, times(1)).getProfile("testuser");
    }

    @Test
    void testLogin_MissingCredentials() {
        // 准备测试数据
        Map<String, String> credentials = new HashMap<>();

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.login(credentials);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Username and password are required", response.getBody().getMessage());
        verify(userService, never()).login(anyString(), anyString());
    }

    @Test
    void testLogin_AuthenticationFailed() {
        // 准备测试数据
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "wrongpassword");

        // 模拟行为
        when(userService.login("testuser", "wrongpassword")).thenThrow(new RuntimeException("Invalid credentials"));

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.login(credentials);

        // 验证结果
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getCode());
        assertEquals("Invalid username or password", response.getBody().getMessage());
        verify(userService, times(1)).login("testuser", "wrongpassword");
    }

    @Test
    void testGetProfile_Success() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setName("Test User");

        // 模拟行为
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userService.getProfile("testuser")).thenReturn(user);

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.getProfile();

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(user, response.getBody().getData());
        verify(userService, times(1)).getProfile("testuser");
    }

    @Test
    void testGetProfile_Unauthorized() {
        // 模拟行为
        when(securityContext.getAuthentication()).thenReturn(null);

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.getProfile();

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getCode());
        assertEquals("unauthorized", response.getBody().getMessage());
        verify(userService, never()).getProfile(anyString());
    }

    @Test
    void testUpdateProfile_Success() {
        // 准备测试数据
        User updatedUser = new User();
        updatedUser.setName("New Name");
        updatedUser.setPhone("0987654321");

        User user = new User();
        user.setUsername("testuser");
        user.setName("New Name");
        user.setPhone("0987654321");

        // 模拟行为
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userService.updateProfile("testuser", updatedUser)).thenReturn(user);

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.updateProfile(updatedUser);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(user, response.getBody().getData());
        verify(userService, times(1)).updateProfile("testuser", updatedUser);
    }

    @Test
    void testUpdateProfile_Unauthorized() {
        // 准备测试数据
        User updatedUser = new User();
        updatedUser.setName("New Name");

        // 模拟行为
        when(securityContext.getAuthentication()).thenReturn(null);

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.updateProfile(updatedUser);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getCode());
        assertEquals("unauthorized", response.getBody().getMessage());
        verify(userService, never()).updateProfile(anyString(), any());
    }

    @Test
    void testUpdatePassword_Success() {
        // 准备测试数据
        Map<String, String> passwordData = new HashMap<>();
        passwordData.put("currentPassword", "oldpassword");
        passwordData.put("newPassword", "newpassword");

        // 模拟HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");

        // 模拟行为
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        doNothing().when(userService).updatePassword("testuser", "newpassword");
        when(jwtUtil.extractUsername("test-token")).thenReturn("testuser");

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.updatePassword(passwordData, request);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        verify(userService, times(1)).updatePassword("testuser", "newpassword");
    }

    @Test
    void testUpdatePassword_Unauthorized() {
        // 准备测试数据
        Map<String, String> passwordData = new HashMap<>();
        passwordData.put("currentPassword", "oldpassword");
        passwordData.put("newPassword", "newpassword");

        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.updatePassword(passwordData, null);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getCode());
        assertEquals("unauthorized", response.getBody().getMessage());
        verify(userService, never()).updatePassword(anyString(), anyString());
    }

    @Test
    void testTestEndpoint() {
        // 执行测试
        ResponseEntity<ResponseDTO<?>> response = userController.test();

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertEquals("Test successful", response.getBody().getData());
    }
}