const BASE_URL = 'http://120.26.3.16:58083';  // 替换为实际的API URL

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

        // 将 token 存储到 Bearer 格式的 Authorization 头部
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // 登录成功，进行后续操作
        alert('Login successful!');
        console.log('Token:', token);

        // 你可以在这里进行页面跳转或其它操作
        window.location.href = 'user-dashboard.html';
    })
    .catch(error => {
        console.error('Login failed:', error);
        document.getElementById('error-message').textContent = 'Invalid username or password';
    });
});
