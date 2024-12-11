document.getElementById('search-form').addEventListener('submit', function (e) {
    e.preventDefault();

    const startTime = document.getElementById('start-time').value;
    const endTime = document.getElementById('end-time').value;
    const attendees = document.getElementById('attendees').value;

    axios.get('/api/meeting-rooms', {
        params: {
            startTime,
            endTime,
            attendees
        }
    }).then(response => {
        const rooms = response.data;
        const roomsList = document.getElementById('available-rooms');
        roomsList.innerHTML = '';
        rooms.forEach(room => {
            roomsList.innerHTML += `<div>Room: ${room.name}, Capacity: ${room.capacity}</div>`;
        });
    });
});
