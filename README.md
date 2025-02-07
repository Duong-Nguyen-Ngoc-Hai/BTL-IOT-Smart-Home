# IoT Smart Home

## Giới thiệu chung
Dự án IoT Smart Home là một hệ thống giám sát và điều khiển các thiết bị trong nhà thông minh. Hệ thống này kết hợp giữa website và phần cứng gồm chip ESP32 cùng các cảm biến để đọc dữ liệu về nhiệt độ, độ ẩm, ánh sáng trong nhà. Đồng thời, hệ thống cũng hỗ trợ điều khiển bật/tắt các thiết bị như đèn, điều hòa từ xa.

## Công cụ và ngôn ngữ sử dụng

### Phần cứng
- **Chip ESP32 NodeMCU**: Bộ điều khiển trung tâm thu thập dữ liệu cảm biến và điều khiển thiết bị.
- **Cảm biến nhiệt độ, độ ẩm DHT11**: Đo nhiệt độ và độ ẩm không khí.
- **Cảm biến ánh sáng BH1750**: Đo cường độ ánh sáng trong nhà.
- **Các thiết bị điện tử khác**:
  - Đèn LED
  - Điện trở
  - Dây điện đực - cái, cái - cái, đực - đực
  - Breadboard (bảng mạch thử nghiệm)

### Phần mềm
- **Backend**: Java Spring Boot.
- **Frontend**: Html, css, JavaScript.
- **Cơ sở dữ liệu**: MySQL (lưu trữ dữ liệu cảm biến, lịch sử hoạt động của các thiết bị).
- **Giao tiếp IoT**: MQTT Mosquitto (để giao tiếp giữa ESP32 và backend).
- **Ngôn ngữ lập trình**:
  - Java (xử lý dữ liệu cảm biến, kết nối MySQL).
  - C++ (lập trình phần cứng chip esp32 với Arduino IDE).

## Tính năng chính
- Đọc và hiển thị dữ liệu cảm biến nhiệt độ, độ ẩm, ánh sáng theo thời gian thực.
- Điều khiển bật/tắt các thiết bị như đèn, điều hòa từ xa qua giao diện web.
- Lưu trữ dữ liệu cảm biến vào cơ sở dữ liệu, duy trì tối đa 500 giá trị mới nhất.
- Giao diện web trực quan với các biểu đồ hiển thị dữ liệu cảm biến, lịch sử dữ liệu thu nhận được và lịch sử hoạt động của các thiết bị.

## Hướng dẫn cài đặt
### 1. Cấu hình phần cứng
- Kết nối ESP32 với cảm biến DHT11, BH1750 và các thiết bị cần điều khiển (trong bài đang để led thay cho tất cả các thiết bị khác).

### 2. Cấu hình phần mềm
- Cài đặt môi trường phát triển cho ESP32 (Arduino IDE).
- Cấu hình MQTT Mosquitto.
- Cài đặt MySQL và khởi tạo database cùng các table cần thiết.
- Chạy code phần cứng trên Arduino, backend  và frontend.

## Đóng góp
Nếu bạn muốn đóng góp cho dự án, vui lòng tạo pull request hoặc mở issue trên GitHub.

