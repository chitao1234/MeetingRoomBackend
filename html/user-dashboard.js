const BASE_URL = 'http://120.26.3.16:58083'; // 替换为实际API URL

// 用户登出
function logout() {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}

// 查询可用会议室
document.getElementById('search-room-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const token = localStorage.getItem('token');
    const startTime = document.getElementById('search-start-time').value;
    const endTime = document.getElementById('search-end-time').value;
    const attendees = document.getElementById('search-attendees').value;

    axios.get(`${BASE_URL}/api/meeting-rooms`, {
        headers: { 'Authorization': `Bearer ${token}` },
        params: { startTime, endTime, attendees }
    }).then(response => {
        const rooms = response.data;
        const roomsList = document.getElementById('available-rooms-list');
        roomsList.innerHTML = '';
        rooms.forEach(room => {
            roomsList.innerHTML += `
                <div>
                    <span>Room: ${room.name}, Capacity: ${room.capacity}</span>
                    <button onclick="bookRoom(${room.meetingRoomId}, '${startTime}', '${endTime}', ${attendees})">Book</button>
                </div>`;
        });
    }).catch(error => {
        console.error('Error fetching rooms:', error);
        alert('Failed to fetch available rooms.');
    });
});

// 预订会议室
function bookRoom(roomId, startTime, endTime, attendees) {
    const token = localStorage.getItem('token');
    const topic = prompt('Enter meeting topic:'); // 获取会议主题

    if (!topic) {
        alert('Meeting topic is required.');
        return;
    }

    axios.post(`${BASE_URL}/api/reservations`, {
        meetingRoomId: roomId,
        startTime,
        endTime,
        participantCount: attendees,
        meetingSubject: topic
    }, {
        headers: { 'Authorization': `Bearer ${token}` }
    }).then(response => {
        alert('Room booked successfully!');
        loadReservations(); // 更新用户预约列表
    }).catch(error => {
        console.error('Error booking room:', error);
        alert('Failed to book room.');
    });
}

// 提交预约
document.getElementById('reservation-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const token = localStorage.getItem('token');
    const startTime = document.getElementById('start-time').value;
    const endTime = document.getElementById('end-time').value;
    const attendees = document.getElementById('attendees').value;
    const topic = document.getElementById('topic').value;

    axios.post(`${BASE_URL}/api/reservations`, {
        startTime,
        endTime,
        participantCount: attendees,
        meetingSubject: topic
    }, {
        headers: { 'Authorization': `Bearer ${token}` }
    }).then(response => {
        alert('Reservation submitted for approval.');
        loadReservations(); // 更新预约列表
    }).catch(error => {
        console.error('Error creating reservation:', error);
        alert('Failed to create reservation.');
    });
});

// 加载用户预约
function loadReservations() {
    const token = localStorage.getItem('token');

    axios.get(`${BASE_URL}/api/reservations/user/{userId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    }).then(response => {
        const reservations = response.data;
        const reservationsList = document.getElementById('reservations-list');
        reservationsList.innerHTML = '';
        reservations.forEach(res => {
            reservationsList.innerHTML += `
                <div>
                    <span>Room: ${res.meetingRoomId}, Status: ${res.status}, Topic: ${res.meetingSubject}</span>
                </div>`;
        });
    }).catch(error => {
        console.error('Error loading reservations:', error);
        alert('Failed to load reservations.');
    });
}

// 初始加载
loadReservations();
