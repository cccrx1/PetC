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

export {
    setToken,
    getToken,
    removeToken,
    setUser,
    getUser,
    removeUser,
    isLoggedIn
};