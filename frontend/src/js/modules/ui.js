// ==================== 页面初始化 ====================
import { isLoggedIn, getUser } from './storage.js';

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

export {
    initPage,
    initCarousel,
    initNavigation,
    initViewMore
};