document.addEventListener('DOMContentLoaded', () => {
    

    let historyData = [];
    let isSearching = false;

    const rowsPerPageSelect = document.getElementById('rowsPerPage');
    const historyBody = document.getElementById('historyBody');
    const pageInfo = document.getElementById('pageInfo');
    const prevPage = document.getElementById('prevPage');
    const nextPage = document.getElementById('nextPage');
    const sortBySelect = document.getElementById('sortBy');
    const deleteButton = document.getElementById('deleteButton');
    const clearButton = document.getElementById('clearButton');
    const searchButton = document.getElementById('searchButton');
    const searchTimestamp = document.getElementById('timestamp');
    const searchDevice = document.getElementById('device');
    const searchAction = document.getElementById('action');

    let currentPage = 1;
    let rowsPerPage = parseInt(rowsPerPageSelect.value);

    // Hàm để lấy dữ liệu từ API
    const fetchHistoryData = async (sortField = 'timestamp', sortOrder = 'desc') => {
        if (isSearching) {

            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/api/historyaction/sorted?sortField=${sortField}&sortOrder=${sortOrder}`);
            const data = await response.json();
            historyData = data; // Lưu trữ dữ liệu từ API
            renderTable();
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const renderTable = () => {
        historyBody.innerHTML = '';
        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const pageHistory = historyData.slice(start, end);

        pageHistory.forEach((row,index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${start + index + 1}</td>
                <td>${row.device}</td>
                <td>${row.action}</td>
                <td>${new Date(row.timestamp).toLocaleString()}</td>
            `;
            historyBody.appendChild(tr);
        });

        pageInfo.textContent = `Page ${currentPage}`;
    };

    const sortData = (sortField, sortOrder) => {
        historyData.sort((a, b) => {
            if (sortOrder === 'asc') {
                return a[sortField] > b[sortField] ? 1 : -1;
            } else {
                return a[sortField] < b[sortField] ? 1 : -1;
            }
        });
        renderTable(); // Hiển thị lại bảng với dữ liệu đã sắp xếp
    };

    rowsPerPageSelect.addEventListener('change', () => {
        rowsPerPage = parseInt(rowsPerPageSelect.value);
        currentPage = 1;
        const sortField = sortBySelect.value.split('_')[0];
        const sortOrder = sortBySelect.value.split('_')[1];
        if (isSearching) {
            sortData(sortField, sortOrder);
        } else {
            fetchHistoryData(sortField, sortOrder);
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
            fetchHistoryData(sortField, sortOrder);
        }
    });

    prevPage.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            const sortField = sortBySelect.value.split('_')[0];
            const sortOrder = sortBySelect.value.split('_')[1];
            if (isSearching) {
                sortData(sortField, sortOrder);
            } else {
                fetchHistoryData(sortField, sortOrder);
            }
            // renderTable();
        }
    });

    nextPage.addEventListener('click', () => {
        if (currentPage < Math.ceil(historyData.length / rowsPerPage)) {
            currentPage++;
            const sortField = sortBySelect.value.split('_')[0];
            const sortOrder = sortBySelect.value.split('_')[1];
            if (isSearching) {
                sortData(sortField, sortOrder);
            } else {
                fetchHistoryData(sortField, sortOrder);
            }
            // renderTable();
        }
    });

    fetchHistoryData();

    // Cập nhật dữ liệu mỗi 10 giây
    setInterval(() => {
        const sortField = sortBySelect.value.split('_')[0];
        const sortOrder = sortBySelect.value.split('_')[1];
        fetchHistoryData(sortField, sortOrder);
    }, 10000);

    deleteButton.addEventListener('click', async () => {
        try {
            const response = await fetch('http://localhost:8080/api/historyaction/deleteAll', {
                method: 'DELETE',
            });

            if (response.ok) {
                // Sau khi xóa thành công, làm mới bảng
                alert("All data has been deleted!");
                historyData = []; // Xóa dữ liệu trên frontend
                renderTable(); // Cập nhật lại bảng
            } else {
                alert("Failed to delete data.");
            }
        } catch (error) {
            console.error('Error deleting data:', error);
        }
    });

    const fetchSearchResults = async () => {
        const device = searchDevice.value || null;
        const action = searchAction.value || null;
        const timestamp = searchTimestamp.value || null;
    
        // Tạo URL với các tham số không null
        const params = new URLSearchParams();
        if (device !== null) params.append('device', device);
        if (action !== null) params.append('action', action);
        if (timestamp !== null) params.append('timestamp', timestamp);
    
        try {
            const response = await fetch(`http://localhost:8080/api/historyaction/search?${params}`);
            if (!response.ok) {
                throw new Error('Failed to fetch search results');
            }
            const data = await response.json();
    
            if (typeof data === 'string') {
                alert(data); // Thông báo nếu không tìm thấy dữ liệu
                historyData = [];
            } else {
                historyData = data; // Lưu kết quả tìm kiếm
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
        searchDevice.value = '';
        searchAction.value = '';
        searchTimestamp.value = '';
    
        // Reset trạng thái tìm kiếm
        isSearching = false;
    
        // Tải lại dữ liệu mặc định
        fetchHistoryData();
    
        // Đặt lại trang đầu
        currentPage = 1;
    
        // Reset Sort By về mặc định (nếu cần)
        sortBySelect.value = 'timestamp_desc';
    });
});
