package com.pethealth.system.service.impl;

import com.pethealth.system.entity.User;
import com.pethealth.system.exception.BusinessException;
import com.pethealth.system.repository.UserRepository;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        return userRepository.save(user);
    }

    @Override
    public String login(String usernameOrEmail, String password) {
        // 直接使用usernameOrEmail进行认证
        // 现在UserDetailsServiceImpl.loadUserByUsername方法已经支持通过邮箱查找用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
        );

        // 设置认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 从认证对象中获取用户名
        String username = authentication.getName();

        // 生成JWT令牌
        return jwtUtil.generateToken(username);
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public User getProfile(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new BusinessException("User not found");
        }
        return userOptional.get();
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public User updateProfile(String username, User updatedUser) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new BusinessException("User not found");
        }

        User user = userOptional.get();
        
        // 更新用户信息
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getAvatar() != null) {
            user.setAvatar(updatedUser.getAvatar());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public void updatePassword(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new BusinessException("User not found");
        }

        User user = userOptional.get();
        // 加密新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}