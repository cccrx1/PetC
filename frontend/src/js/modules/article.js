// ==================== 文章交互功能 ====================
import { isLoggedIn } from './storage.js';
import { apiRequest } from './api.js';

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

export {
    initArticleInteractions
};