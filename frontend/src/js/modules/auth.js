// ==================== 用户认证功能 ====================
import { setToken, setUser, removeToken, removeUser } from './storage.js';
import { API_BASE_URL } from './api.js';

async function login(email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: email, password })
        });
        
        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || '登录失败');
        }
        
        const data = await response.json();
        
        // 保存token和用户信息
        setToken(data.token);
        setUser(data.user);
        
        return data;
    } catch (error) {
        console.error('登录错误:', error);
        throw error;
    }
}

async function register(name, email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ 
                name, 
                username: email, 
                email, 
                password 
            })
        });
        
        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || '注册失败');
        }
        
        return await response.json();
    } catch (error) {
        console.error('注册错误:', error);
        throw error;
    }
}

function logout() {
    removeToken();
    removeUser();
    window.location.href = 'login.html';
}

export {
    login,
    register,
    logout
};