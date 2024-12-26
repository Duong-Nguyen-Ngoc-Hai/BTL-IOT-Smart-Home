document.getElementById('yardLightButton').addEventListener('click', function() {
    toggleDevice('yardLightButton');
});

document.getElementById('gateLightButton').addEventListener('click', function() {
    toggleDevice('gateLightButton');
});

let client;

document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.on-off-button');
    client = mqtt.connect('ws://localhost:9001', {
        username: 'duong', // Username nếu có
        password: '12345'  // Password nếu có
    });

    client.on('connect', () => {
        console.log('Connected to MQTT Broker');
        client.subscribe('esp32/led/status', (err) => {
            if (err) {
                console.error('Failed to subscribe to esp32/led/status:', err);
            } else {
                console.log('Subscribed to esp32/led/status');
            }
        });
    });

    client.on('message', (topic, payload) => {
        console.log(`Received message from topic "${topic}": ${payload.toString()}`);
        if (topic === 'esp32/led/status') {
            const status = JSON.parse(payload.toString());
    
            // Cập nhật trạng thái nút và giao diện
            ['yardLightButton', 'gateLightButton'].forEach((buttonId) => {
                const button = document.getElementById(buttonId);
                const icon = button.parentElement.querySelector('i');
                const device = buttonId.replace('Button', '');
                
                if(status[device]) {
                    if (status[device] === 'OFF') {
                        button.innerText = 'On';
                        button.dataset.state = 'On';
                        button.classList.add('on');
                        button.classList.remove('off');
                        icon.classList.add('off-icon');
                        icon.classList.remove('on-icon');
                    } else {
                        button.innerText = 'Off';
                        button.classList.add('off');
                        button.dataset.state = 'Off';
                        button.classList.remove('on');
                        icon.classList.add('on-icon');
                        icon.classList.remove('off-icon');
                    }
                    button.disabled = false;
                }
            });
        }
    });
    
    buttons.forEach(button => {
        const state = localStorage.getItem(button.id) || 'Off';
        const icon = button.parentElement.querySelector('i');
        button.innerText = state;
        if (state === 'On') {
            button.classList.remove('off');
            button.classList.add('on');
            button.textContent = 'On';

            icon.classList.remove('on-icon');
            icon.classList.add('off-icon');
        } else {
            button.classList.remove('on');
            button.classList.add('off');
            button.textContent = 'Off';

            icon.classList.remove('off-icon');
            icon.classList.add('on-icon');
        }

        button.replaceWith(button.cloneNode(true));
        button = document.getElementById(button.id);

        button.addEventListener('click', () => {
            toggleDevice(button.id);
        });
    });
});

function toggleDevice(buttonId) {
    const button = document.getElementById(buttonId);
    const device = buttonId.replace('Button', '');
    const icon = button.parentElement.querySelector('i');
    let message = '';

    const currentState = button.innerText;
    // Hiển thị trạng thái đang xử lý
    button.innerText = 'Processing...';
    button.disabled = true; // Ngăn người dùng nhấn nhiều lần

    let state;
    if (currentState === 'Off') {
        state = "On";
        button.dataset.state = 'On';
        if (device === 'yardLight') {
            message = 'OFF4';
        } else {
            message = 'OFF5';
        }
        // button.innerText = 'On';
        // button.classList.remove('off');
        // button.classList.add('on');
        // icon.classList.remove('on-icon');
        // icon.classList.add('off-icon');
    } else {
        state = "Off";
        button.dataset.state = 'Off';
        if (device === 'yardLight') {
            message = 'ON4';
        } else {
            message = 'ON5';
        }
        // button.innerText = 'Off';
        // button.classList.remove('on');
        // button.classList.add('off');
        // icon.classList.remove('off-icon');
        // icon.classList.add('on-icon');
    }

    localStorage.setItem(buttonId, state);
    client.publish('esp32/led', message, { qos: 1, retain: false }, (err) => {
        if (err) {
            console.error('Failed to send message:', err);
        } else {
            console.log(`Message "${message}" sent to topic "esp32/led"`);
        }
    });
}

function getRandomValue(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// Sample data for chart
document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('outsideTempChart').getContext('2d');
    const outsideTempChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels:[], // Trục hoành theo 24 giờ
            datasets: [{
                label: 'Outside Temperature',
                data: [],
                borderColor: 'green',
                fill: false
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Time (hours)'
                    }
                },
                y: {
                    beginAtZero: true,
                    max : 50,
                    ticks: {
                        display: true,
                        stepSize: 10,
                        text: 'Temperature (°C)'
                    }
                }
            }
        }
    });
    function updateChartData() {
        fetch('http://localhost:8080/api/sensor_data/top10')
            .then(response => response.json())
            .then(data => {
                const labels = data.map(item => new Date(item.timestamp).toLocaleTimeString());
                const outTemperatureData = data.map(item => item.outtemp);

                outsideTempChart.data.labels = labels.reverse();
                outsideTempChart.data.datasets[0].data = outTemperatureData.reverse();
                outsideTempChart.update();

                // Cập nhật giá trị trong parameter-box
                document.querySelector('.parameter-box.temperature .value').innerText = `${outTemperatureData[outTemperatureData.length - 1]}°C`;
            })
            .catch(error => console.error('Error fetching data:', error));
    }

    // Cập nhật dữ liệu mỗi 10 giây
    setInterval(updateChartData, 10000);

    // Cập nhật dữ liệu ngay khi tải trang
    updateChartData();
});
