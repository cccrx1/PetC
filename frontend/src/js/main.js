// 轮播图功能
const carousel = document.querySelector('.carousel-inner');
const carouselItems = document.querySelectorAll('.carousel-item');
const prevBtn = document.querySelector('.carousel-inner + button');
const nextBtn = document.querySelector('.carousel-inner + button + button');
let currentIndex = 0;
const itemWidth = 100; // 百分比

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

// 导航菜单切换
const navLinks = document.querySelectorAll('nav a');
navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        // 移除所有链接的活跃状态
        navLinks.forEach(l => l.classList.remove('text-blue-600'));
        navLinks.forEach(l => l.classList.add('text-gray-700'));
        // 添加当前链接的活跃状态
        link.classList.remove('text-gray-700');
        link.classList.add('text-blue-600');
    });
});

// 模拟登录按钮点击
const loginBtn = document.querySelector('header button');
if (loginBtn) {
    loginBtn.addEventListener('click', () => {
        Swal.fire({
            title: '登录',
            html: `
                <input type="text" id="email" class="swal2-input" placeholder="邮箱">
                <input type="password" id="password" class="swal2-input" placeholder="密码">
            `,
            showCancelButton: true,
            confirmButtonText: '登录',
            cancelButtonText: '取消',
            preConfirm: () => {
                const email = document.getElementById('email').value;
                const password = document.getElementById('password').value;
                if (!email || !password) {
                    Swal.showValidationMessage('请输入邮箱和密码');
                }
                return { email, password };
            }
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire('登录成功！', '', 'success');
            }
        });
    });
}

// 文章点赞功能
const likeButtons = document.querySelectorAll('.flex.space-x-3 button:first-child');
likeButtons.forEach(button => {
    button.addEventListener('click', () => {
        if (button.classList.contains('text-blue-600')) {
            button.classList.remove('text-blue-600');
            button.classList.add('text-gray-500');
        } else {
            button.classList.remove('text-gray-500');
            button.classList.add('text-blue-600');
        }
    });
});

// 文章收藏功能
const collectButtons = document.querySelectorAll('.flex.space-x-3 button:last-child');
collectButtons.forEach(button => {
    button.addEventListener('click', () => {
        if (button.classList.contains('text-blue-600')) {
            button.classList.remove('text-blue-600');
            button.classList.add('text-gray-500');
        } else {
            button.classList.remove('text-gray-500');
            button.classList.add('text-blue-600');
        }
    });
});