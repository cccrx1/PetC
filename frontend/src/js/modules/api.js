// ==================== 全局配置 ====================
const API_BASE_URL = '/api';

// ==================== API请求工具函数 ====================
import { getToken, logout } from './storage.js';

async function apiRequest(url, options = {}) {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    
    try {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 10000);
        
        const response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers,
            signal: controller.signal
        });
        
        clearTimeout(timeoutId);
        
        if (response.status === 401) {
            // 未授权，清除登录状态并跳转到登录页
            logout();
            window.location.href = 'login.html';
            return null;
        }
        
        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || '请求失败');
        }
        
        // 处理204 No Content响应
        if (response.status === 204) {
            return null;
        }
        
        return await response.json();
    } catch (error) {
        console.error('API请求错误:', error);
        throw error;
    }
}

export {
    apiRequest,
    API_BASE_URL
};