package com.pethealth.system.service;

import com.pethealth.system.entity.User;

public interface UserService {
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功的用户
     */
    User register(User user);
    
    /**
     * 用户登录
     * @param usernameOrEmail 用户名或邮箱
     * @param password 密码
     * @return JWT令牌
     */
    String login(String usernameOrEmail, String password);
    
    /**
     * 获取用户资料
     * @param username 用户名
     * @return 用户资料
     */
    User getProfile(String username);
    
    /**
     * 更新用户资料
     * @param username 用户名
     * @param updatedUser 更新的用户信息
     * @return 更新后的用户
     */
    User updateProfile(String username, User updatedUser);
    
    /**
     * 更新密码
     * @param username 用户名
     * @param newPassword 新密码
     */
    void updatePassword(String username, String newPassword);
}