// ==================== 导入模块 ====================
import { login, register, logout } from './modules/auth.js';
import { isLoggedIn, getUser } from './modules/storage.js';
import { apiRequest } from './modules/api.js';
import { initPage, initCarousel, initNavigation, initViewMore } from './modules/ui.js';
import { initArticleInteractions } from './modules/article.js';

// ==================== 页面加载完成后初始化 ====================
document.addEventListener('DOMContentLoaded', () => {
    initPage();
    initCarousel();
    initNavigation();
    initArticleInteractions();
    initViewMore();
});

// ==================== 导出全局函数 ====================
window.auth = {
    login,
    register,
    logout,
    isLoggedIn,
    getUser,
    apiRequest
};
