# Đồ án Bảo mật Android: Tấn công MitM & SSL Certificate Pinning

Dự án này là một bài Demo thực hành môn An toàn thông tin / Bảo mật trên nền tảng Android. 
Mục tiêu của đồ án là chứng minh lỗ hổng **Man-in-the-Middle (MitM)** khi ứng dụng tin tưởng các chứng chỉ không an toàn (User CA) và cách phòng chống triệt để bằng kỹ thuật **SSL Certificate Pinning**.

## 📁 Cấu trúc thư mục (Monorepo)

- `app/` & các thư mục gốc: Chứa Source code của ứng dụng Android Client (viết bằng Kotlin).
- `Backend/`: Chứa mã nguồn Server API đơn giản (viết bằng Node.js + Express).

---

## 🚀 Hướng dẫn Cài đặt & Khởi chạy

### 1. Khởi chạy Backend Server
Cần có [Node.js](https://nodejs.org/) được cài đặt trên máy.

1. Mở Terminal, di chuyển vào thư mục Backend:
   ```bash
   cd Backend
   ```
2. Cài đặt các thư viện cần thiết:
   ```bash
   npm install
   ```
3. Chạy Server (Mặc định chạy ở cổng 3000):
   ```bash
   node index.js
   ```
4. **Cấp phát HTTPS (Bắt buộc cho Demo):** Mở một Terminal mới và chạy ngrok:
   ```bash
   npx ngrok http 3000
   ```
   *(Copy đường link `https://...ngrok-free.app` để cập nhật vào Client).*

### 2. Khởi chạy Android Client
1. Mở dự án này bằng **Android Studio**.
2. Mở file `app/src/main/java/com/example/myapplication/RetrofitClient.kt`.
3. Cập nhật biến `BASE_URL` thành đường link HTTPS của ngrok vừa lấy ở trên.
4. *(Tùy chọn cho Pinning)*: Thay đổi mã SHA-256 trong code thành mã Hash thực tế của đường link ngrok.
5. Cắm điện thoại hoặc bật Emulator, bấm **Run** để cài đặt App.

---

## 🎯 Kịch bản Test (Dành cho Giảng viên/Người chấm)

Bài test gồm 2 giai đoạn chính, được thực hiện kết hợp với phần mềm **Burp Suite Community Edition**.

### Giai đoạn 1: Tấn công Man-in-the-Middle (MitM)
1. Trên điện thoại: Cài đặt chứng chỉ PortSwigger CA (Tải từ `http://burp`) vào mục *User Certificates*.
2. Cài đặt Wifi Proxy trên điện thoại trỏ về IP của máy tính chạy Burp Suite (Cổng 8080).
3. Trên Burp Suite: Bật **Intercept is ON**.
4. Trên App Android: **TẮT** công tắc "Bật SSL Certificate Pinning".
5. Bấm gửi một tin nhắn chứa dữ liệu nhạy cảm (VD: lệnh chuyển tiền).
6. **Kết quả:** Burp Suite sẽ bắt được nguyên bản nội dung tin nhắn (Plaintext). Hacker có thể sửa đổi dữ liệu (VD: đổi số tiền) trước khi Forward lên Server.

### Giai đoạn 2: Phòng chống với SSL Certificate Pinning
1. Vẫn giữ nguyên cấu hình Proxy và đang mở Burp Suite như Giai đoạn 1.
2. Trên App Android: **BẬT** công tắc "Bật SSL Certificate Pinning".
3. Thực hiện gửi lại tin nhắn.
4. **Kết quả:** Gói tin bị ngắt từ trong trứng nước. App Android sẽ báo lỗi đỏ: `Certificate pinning failure!`. Trên Burp Suite báo lỗi `SSL Handshake Failed`. 
5. **Giải thích:** Nhờ SSL Pinning, App Android chỉ tin tưởng duy nhất chứng chỉ của Server ngrok. Khi Burp Suite chặn ở giữa và đưa ra chứng chỉ PortSwigger, App nhận thấy mã Hash không khớp và lập tức hủy kết nối để bảo vệ an toàn dữ liệu.

---
*Dự án hoàn thiện phục vụ cho bài bảo vệ Final môn học.*
