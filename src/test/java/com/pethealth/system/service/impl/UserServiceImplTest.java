package com.pethealth.system.service.impl;

import com.pethealth.system.entity.User;
import com.pethealth.system.exception.BusinessException;
import com.pethealth.system.repository.UserRepository;
import com.pethealth.system.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // 执行测试
        User result = userService.register(user);

        // 验证结果
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegister_UsernameExists() {
        // 准备测试数据
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // 模拟行为
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register(user);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("existinguser");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_EmailExists() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("existing@example.com");
        user.setPassword("password123");

        // 模拟行为
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register(user);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        String username = "testuser";
        String password = "password123";
        String token = "test-token";

        // 模拟行为
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(jwtUtil.generateToken(username)).thenReturn(token);

        // 执行测试
        String result = userService.login(username, password);

        // 验证结果
        assertEquals(token, result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication, times(1)).getName();
        verify(jwtUtil, times(1)).generateToken(username);
    }

    @Test
    void testGetProfile_Success() {
        // 准备测试数据
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setName("Test User");

        // 模拟行为
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // 执行测试
        User result = userService.getProfile(username);

        // 验证结果
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("Test User", result.getName());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetProfile_UserNotFound() {
        // 准备测试数据
        String username = "nonexistent";

        // 模拟行为
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.getProfile(username);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testUpdateProfile_Success() {
        // 准备测试数据
        String username = "testuser";
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setName("Old Name");
        existingUser.setPhone("1234567890");

        User updatedUser = new User();
        updatedUser.setName("New Name");
        updatedUser.setPhone("0987654321");

        // 模拟行为
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // 执行测试
        User result = userService.updateProfile(username, updatedUser);

        // 验证结果
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("0987654321", result.getPhone());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateProfile_UserNotFound() {
        // 准备测试数据
        String username = "nonexistent";
        User updatedUser = new User();
        updatedUser.setName("New Name");

        // 模拟行为
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateProfile(username, updatedUser);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdatePassword_Success() {
        // 准备测试数据
        String username = "testuser";
        String newPassword = "newpassword123";
        User user = new User();
        user.setUsername(username);

        // 模拟行为
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);

        // 执行测试
        userService.updatePassword(username, newPassword);

        // 验证结果
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        // 准备测试数据
        String username = "nonexistent";
        String newPassword = "newpassword123";

        // 模拟行为
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updatePassword(username, newPassword);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }
}