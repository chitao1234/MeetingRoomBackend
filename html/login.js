const BASE_URL = 'http://localhost:8080';  // 替换为实际的API URL

document.getElementById('login-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // 登录请求
    axios.post(`${BASE_URL}/api/auth/login`, {
        username: username,
        password: password
    })
    .then(response => {
        const token = response.data.token;
        const userId = response.data.userId; // 假设服务端返回的用户ID字段为 userId

        // 将 token 和 userId 存储到本地存储
        localStorage.setItem('token', token);
        localStorage.setItem('userId', userId);

        // 设置默认的 Authorization 头部
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // 登录成功，进行后续操作
        alert('Login successful!');
        console.log('Token:', token, 'UserID:', userId);

        // 页面跳转到用户仪表板
        window.location.href = 'user-dashboard.html';
    })
    .catch(error => {
        console.error('Login failed:', error);
        document.getElementById('error-message').textContent = 'Invalid username or password';
    });
});
