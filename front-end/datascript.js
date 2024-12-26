document.addEventListener('DOMContentLoaded', () => {
    const historyBody = document.getElementById('dataBody'); // Bảng hiển thị dữ liệu
    const rowsPerPageSelect = document.getElementById('rowsPerPage');
    const pageInfo = document.getElementById('pageInfo');
    const prevPage = document.getElementById('prevPage');
    const nextPage = document.getElementById('nextPage');
    const sortBySelect = document.getElementById('sortBy');
    const deleteButton = document.getElementById('deleteButton');
    const searchTemperature = document.getElementById('temperature');
    const searchHumidity = document.getElementById('humidity');
    const searchLux = document.getElementById('lux');
    const searchTimestamp = document.getElementById('timestamp');
    const searchOuttemp = document.getElementById('outtemp');
    const searchButton = document.getElementById('searchButton');
    const clearButton = document.getElementById('clearButton');

    let currentPage = 1;
    let rowsPerPage = parseInt(rowsPerPageSelect.value);
    let sensorData = [];
    let isSearching = false;

    // Hàm để lấy dữ liệu từ API
    const fetchSensorData = async (sortField = 'timestamp', sortOrder = 'desc') => {
        if (isSearching) {

            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/api/sensor_data/sorted?sortField=${sortField}&sortOrder=${sortOrder}`);
            const data = await response.json();
            sensorData = data; // Lưu trữ dữ liệu từ API
            renderTable();
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    // Hàm để render bảng
    const renderTable = () => {
        historyBody.innerHTML = '';
        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const pageData = sensorData.slice(start, end);

        pageData.forEach((row, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${start + index + 1}</td>
                <td>${row.temperature} °C</td>
                <td>${row.humidity} %</td>
                <td>${row.lux} lux</td>
                <td>${row.outtemp} °C</td>
                <td>${new Date(row.timestamp).toLocaleString()}</td>
            `;
            historyBody.appendChild(tr);
        });

        pageInfo.textContent = `Page ${currentPage}`;
    };

    const sortData = (sortField, sortOrder) => {
        sensorData.sort((a, b) => {
            if (sortOrder === 'asc') {
                return a[sortField] > b[sortField] ? 1 : -1;
            } else {
                return a[sortField] < b[sortField] ? 1 : -1;
            }
        });
        renderTable(); // Hiển thị lại bảng với dữ liệu đã sắp xếp
    };

    // Cập nhật lại số hàng hiển thị khi thay đổi lựa chọn
    rowsPerPageSelect.addEventListener('change', () => {
        rowsPerPage = parseInt(rowsPerPageSelect.value);
        currentPage = 1;
        const sortField = sortBySelect.value.split('_')[0];
        const sortOrder = sortBySelect.value.split('_')[1];
        if (isSearching) {
            sortData(sortField, sortOrder);
        } else {
            fetchSensorData(sortField, sortOrder);
        }
        // renderTable();
    });

    sortBySelect.addEventListener('change', () => {
        currentPage = 1;
        const sortField = sortBySelect.value.split('_')[0];
        const sortOrder = sortBySelect.value.split('_')[1];
        if (isSearching) {
            sortData(sortField, sortOrder);
        } else {
            fetchSensorData(sortField, sortOrder);
        }
    });

    // Điều hướng trang
    prevPage.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            const sortField = sortBySelect.value.split('_')[0];
            const sortOrder = sortBySelect.value.split('_')[1];
            if (isSearching) {
                sortData(sortField, sortOrder);
            } else {
                fetchSensorData(sortField, sortOrder);
            }
            // renderTable();
        }
    });

    nextPage.addEventListener('click', () => {
        if (currentPage < Math.ceil(sensorData.length / rowsPerPage)) {
            currentPage++;
            const sortField = sortBySelect.value.split('_')[0];
            const sortOrder = sortBySelect.value.split('_')[1];
            if (isSearching) {
                sortData(sortField, sortOrder);
            } else {
                fetchSensorData(sortField, sortOrder);
            }
            // renderTable();
        }
    });

    // Gọi API để lấy dữ liệu khi tải trang
    fetchSensorData();

    // Cập nhật dữ liệu mỗi 10 giây
    setInterval(() => {
        const sortField = sortBySelect.value.split('_')[0];
        const sortOrder = sortBySelect.value.split('_')[1];
        fetchSensorData(sortField, sortOrder);
    }, 10000);

    deleteButton.addEventListener('click', async () => {
        try {
            const response = await fetch('http://localhost:8080/api/sensor_data/deleteAll', {
                method: 'DELETE',
            });

            if (response.ok) {
                // Sau khi xóa thành công, làm mới bảng
                alert("All data has been deleted!");
                sensorData = []; // Xóa dữ liệu trên frontend
                renderTable(); // Cập nhật lại bảng
            } else {
                alert("Failed to delete data.");
            }
        } catch (error) {
            console.error('Error deleting data:', error);
        }
    });

    const fetchSearchResults = async () => {
        const temperature = searchTemperature.value || null;
        const humidity = searchHumidity.value || null;
        const lux = searchLux.value || null;
        const outtemp = searchOuttemp.value || null;
        const timestamp = searchTimestamp.value || null;
    
        // Tạo URL với các tham số không null
        const params = new URLSearchParams();
        if (temperature !== null) params.append('temperature', temperature);
        if (humidity !== null) params.append('humidity', humidity);
        if (lux !== null) params.append('lux', lux);
        if (timestamp !== null) params.append('timestamp', timestamp);
        if (outtemp !== null && temperature==null && humidity==null && lux==null) params.append('outtemp', outtemp);
    
        try {
            const response = await fetch(`http://localhost:8080/api/sensor_data/search?${params}`);
            if (!response.ok) {
                throw new Error('Failed to fetch search results');
            }
            const data = await response.json();
    
            if (typeof data === 'string') {
                alert(data); // Thông báo nếu không tìm thấy dữ liệu
                sensorData = [];
            } else {
                sensorData = data; // Lưu kết quả tìm kiếm
            }
            sortData(sortBySelect.value.split('_')[0], sortBySelect.value.split('_')[1]);
            isSearching = true; // Đánh dấu đang tìm kiếm
            currentPage = 1; // Reset về trang đầu
            renderTable(); // Hiển thị dữ liệu
        } catch (error) {
            console.error('Error fetching search results:', error);
            alert('An error occurred while searching for data.');
        }
    };

    searchButton.addEventListener('click', (e) => {
        e.preventDefault();
        fetchSearchResults();
    });

    clearButton.addEventListener('click', () => {
        // Xóa giá trị trong các ô tìm kiếm
        searchTemperature.value = '';
        searchHumidity.value = '';
        searchLux.value = '';
        searchOuttemp.value = '';
        searchTimestamp.value = '';
    
        // Reset trạng thái tìm kiếm
        isSearching = false;
    
        // Tải lại dữ liệu mặc định
        fetchSensorData();
    
        // Đặt lại trang đầu
        currentPage = 1;
    
        // Reset Sort By về mặc định (nếu cần)
        sortBySelect.value = 'timestamp_desc';
    });
});
