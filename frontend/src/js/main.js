// ==================== 全局配置 ====================
const API_BASE_URL = 'http://localhost:8080/api';

// ==================== 本地存储工具函数 ====================
function setToken(token) {
    localStorage.setItem('jwt_token', token);
}

function getToken() {
    return localStorage.getItem('jwt_token');
}

function removeToken() {
    localStorage.removeItem('jwt_token');
}

function setUser(user) {
    if (user) {
        localStorage.setItem('user', JSON.stringify(user));
    }
}

function getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
}

function removeUser() {
    localStorage.removeItem('user');
}

function isLoggedIn() {
    return !!getToken();
}

// ==================== API请求工具函数 ====================
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

// ==================== 用户认证功能 ====================
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

// ==================== 页面初始化 ====================
function initPage() {
    // 更新登录状态显示
    updateAuthUI();
    
    // 检查是否需要登录
    checkAuthRequired();
}

function updateAuthUI() {
    const user = getUser();
    const loginBtn = document.querySelector('header button');
    
    if (loginBtn) {
        if (isLoggedIn() && user) {
            loginBtn.textContent = user.name || user.username || '我的';
            loginBtn.onclick = () => {
                window.location.href = 'profile.html';
            };
        } else {
            loginBtn.textContent = '登录';
            loginBtn.onclick = () => {
                window.location.href = 'login.html';
            };
        }
    }
}

function checkAuthRequired() {
    // 需要登录才能访问的页面
    const authRequiredPages = ['pets.html', 'consultation.html', 'profile.html', 'health.html'];
    const currentPage = window.location.pathname.split('/').pop();
    
    if (authRequiredPages.includes(currentPage) && !isLoggedIn()) {
        window.location.href = 'login.html';
    }
}

// ==================== 轮播图功能 ====================
function initCarousel() {
    const carousel = document.querySelector('.carousel-inner');
    const carouselItems = document.querySelectorAll('.carousel-item');
    const prevBtn = document.querySelector('.carousel-prev');
    const nextBtn = document.querySelector('.carousel-next');
    
    if (!carousel || carouselItems.length === 0) return;
    
    let currentIndex = 0;
    const itemWidth = 100;
    
    function updateCarousel() {
        carousel.style.transform = `translateX(-${currentIndex * itemWidth}%)`;
    }
    
    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
            currentIndex = (currentIndex + 1) % carouselItems.length;
            updateCarousel();
        });
    }
    
    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
            currentIndex = (currentIndex - 1 + carouselItems.length) % carouselItems.length;
            updateCarousel();
        });
    }
    
    // 自动轮播
    setInterval(() => {
        currentIndex = (currentIndex + 1) % carouselItems.length;
        updateCarousel();
    }, 5000);
}

// ==================== 导航菜单功能 ====================
function initNavigation() {
    // 高亮当前页面导航
    const currentPage = window.location.pathname.split('/').pop() || 'index.html';
    const navLinks = document.querySelectorAll('nav a');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPage) {
            link.classList.remove('text-gray-700');
            link.classList.add('text-blue-600');
        } else {
            link.classList.remove('text-blue-600');
            link.classList.add('text-gray-700');
        }
    });
}

// ==================== 文章交互功能 ====================
function initArticleInteractions() {
    // 文章点赞功能
    const likeButtons = document.querySelectorAll('.article-like-btn');
    likeButtons.forEach(button => {
        button.addEventListener('click', async (e) => {
            e.preventDefault();
            
            if (!isLoggedIn()) {
                Swal.fire({
                    title: '请先登录',
                    text: '登录后才能点赞',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: '去登录',
                    cancelButtonText: '取消'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = 'login.html';
                    }
                });
                return;
            }
            
            const articleId = button.dataset.articleId;
            if (!articleId) return;
            
            try {
                await apiRequest(`/articles/${articleId}/like`, {
                    method: 'POST'
                });
                
                // 切换按钮状态
                if (button.classList.contains('text-blue-600')) {
                    button.classList.remove('text-blue-600');
                    button.classList.add('text-gray-500');
                } else {
                    button.classList.remove('text-gray-500');
                    button.classList.add('text-blue-600');
                }
                
                Swal.fire('操作成功', '', 'success');
            } catch (error) {
                Swal.fire('操作失败', error.message, 'error');
            }
        });
    });
    
    // 文章收藏功能
    const collectButtons = document.querySelectorAll('.article-collect-btn');
    collectButtons.forEach(button => {
        button.addEventListener('click', async (e) => {
            e.preventDefault();
            
            if (!isLoggedIn()) {
                Swal.fire({
                    title: '请先登录',
                    text: '登录后才能收藏',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: '去登录',
                    cancelButtonText: '取消'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = 'login.html';
                    }
                });
                return;
            }
            
            const articleId = button.dataset.articleId;
            if (!articleId) return;
            
            try {
                await apiRequest(`/articles/${articleId}/collect`, {
                    method: 'POST'
                });
                
                // 切换按钮状态
                if (button.classList.contains('text-blue-600')) {
                    button.classList.remove('text-blue-600');
                    button.classList.add('text-gray-500');
                } else {
                    button.classList.remove('text-gray-500');
                    button.classList.add('text-blue-600');
                }
                
                Swal.fire('操作成功', '', 'success');
            } catch (error) {
                Swal.fire('操作失败', error.message, 'error');
            }
        });
    });
}

// ==================== 查看更多功能 ====================
function initViewMore() {
    const viewMoreBtn = document.getElementById('view-more-btn');
    if (viewMoreBtn) {
        viewMoreBtn.addEventListener('click', function(e) {
            e.preventDefault();
            Swal.fire({
                title: '查看更多',
                text: '更多养宠科普文章正在更新中，敬请期待！',
                icon: 'info'
            });
        });
    }
}

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
