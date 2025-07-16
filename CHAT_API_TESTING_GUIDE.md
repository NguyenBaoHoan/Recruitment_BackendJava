# Hướng Dẫn Test API Chat - JobHunter

## Tổng Quan
Dự án JobHunter có các API Chat sau:
1. **GET** `/chat/rooms/{roomId}/messages` - Lấy lịch sử tin nhắn
2. **GET** `/chat/users/{userId}/rooms` - Lấy danh sách phòng chat của user
3. **POST** `/chat/send-message` - Gửi tin nhắn (REST endpoint cho testing)
4. **WebSocket** `/app/chat.sendMessage` - Gửi tin nhắn qua WebSocket

## Cài Đặt Postman Collection

### Bước 1: Import Collection
1. Mở Postman
2. Click "Import" 
3. Chọn file `Chat_API_Tests.postman_collection.json`
4. Collection sẽ được import với tên "Chat API Tests"

### Bước 2: Cấu Hình Environment Variables
Trong collection, các biến môi trường đã được cấu hình:
- `base_url`: `http://localhost:8080`
- `room_id`: `1_2` (phòng chat giữa user 1 và 2)
- `user_id`: `1` (ID của user)

## Chi Tiết Test Cases

### 1. Test Lấy Lịch Sử Tin Nhắn
**Request:**
```
GET {{base_url}}/chat/rooms/{{room_id}}/messages
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "chatRoomId": "1_2",
    "senderId": 1,
    "senderDisplayName": "John Doe",
    "content": "Hello!",
    "timestamp": "2024-01-15T10:30:00Z",
    "messageType": "TEXT"
  }
]
```

### 2. Test Lấy Danh Sách Phòng Chat
**Request:**
```
GET {{base_url}}/chat/users/{{user_id}}/rooms
```

**Expected Response:**
```json
[
  {
    "id": "1_2",
    "otherParticipantName": "Jane Smith",
    "otherParticipantPhotoUrl": "https://example.com/photo.jpg",
    "lastMessage": "Hello!",
    "lastMessageTimestamp": "2024-01-15T10:30:00Z"
  }
]
```

### 3. Test Gửi Tin Nhắn
**Request:**
```
POST {{base_url}}/chat/send-message
Content-Type: application/json

{
  "content": "Hello! This is a test message.",
  "senderId": 1,
  "recipientId": 2,
  "messageType": "TEXT"
}
```

**Expected Response:**
```json
{
  "id": 2,
  "chatRoomId": "1_2",
  "senderId": 1,
  "senderDisplayName": "John Doe",
  "content": "Hello! This is a test message.",
  "timestamp": "2024-01-15T10:35:00Z",
  "messageType": "TEXT"
}
```

### 4. Test Các Loại Tin Nhắn Khác
**Message Types có sẵn:**
- `TEXT` - Tin nhắn văn bản
- `IMAGE` - Tin nhắn hình ảnh
- `AUDIO` - Tin nhắn âm thanh
- `VIDEO` - Tin nhắn video
- `FILE` - Tin nhắn file
- `LOCATION` - Tin nhắn vị trí
- `OTHER` - Loại khác

**Ví dụ test IMAGE:**
```json
{
  "content": "https://example.com/image.jpg",
  "senderId": 1,
  "recipientId": 2,
  "messageType": "IMAGE"
}
```

## Chuẩn Bị Dữ Liệu Test

### 1. Đảm Bảo Database Có Dữ Liệu
Trước khi test, cần có:
- Users trong bảng `users` (ít nhất user ID 1 và 2)
- Có thể có sẵn một số tin nhắn trong phòng chat

### 2. SQL Script Để Tạo Dữ Liệu Test
```sql
-- Tạo users test (nếu chưa có)
INSERT INTO users (id, display_name, email) VALUES 
(1, 'John Doe', 'john@example.com'),
(2, 'Jane Smith', 'jane@example.com');

-- Tạo phòng chat test
INSERT INTO chat_rooms (id, last_updated) VALUES ('1_2', NOW());

-- Thêm participants vào phòng chat
INSERT INTO chat_room_participants (chat_room_id, user_id) VALUES 
('1_2', 1),
('1_2', 2);

-- Tạo một số tin nhắn test
INSERT INTO messages (content, created_at, message_type, is_read, chat_room_id, sender_id) VALUES 
('Hello Jane!', NOW(), 'TEXT', false, '1_2', 1),
('Hi John!', NOW(), 'TEXT', false, '1_2', 2);
```

## Test WebSocket (Tùy Chọn)

### Sử Dụng WebSocket Client
Để test WebSocket endpoint `/app/chat.sendMessage`, bạn có thể:

1. **Sử dụng Postman WebSocket:**
   - Tạo WebSocket request trong Postman
   - Connect tới: `ws://localhost:8080/ws`
   - Subscribe tới topic: `/topic/rooms/1_2`
   - Send message tới: `/app/chat.sendMessage`

2. **Sử dụng Online WebSocket Tester:**
   - Truy cập: https://www.piesocket.com/websocket-tester
   - Connect tới: `ws://localhost:8080/ws`
   - Subscribe và send messages

### WebSocket Message Format
```json
{
  "content": "Hello via WebSocket!",
  "senderId": 1,
  "recipientId": 2,
  "messageType": "TEXT"
}
```

## Troubleshooting

### Lỗi Thường Gặp

1. **404 Not Found:**
   - Kiểm tra server có đang chạy không
   - Kiểm tra port 8080 có đúng không
   - Kiểm tra URL path có đúng không

2. **500 Internal Server Error:**
   - Kiểm tra database connection
   - Kiểm tra dữ liệu users có tồn tại không
   - Xem logs của Spring Boot

3. **400 Bad Request:**
   - Kiểm tra format JSON có đúng không
   - Kiểm tra các field bắt buộc có đầy đủ không
   - Kiểm tra messageType có hợp lệ không

### Debug Tips

1. **Enable SQL Logging:**
   Trong `application.properties`:
   ```properties
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

2. **Check Application Logs:**
   ```bash
   # Xem logs real-time
   tail -f logs/application.log
   ```

3. **Test Database Connection:**
   ```bash
   # Kiểm tra MySQL connection
   mysql -u root -p jobhunter
   ```

## Kết Luận

Collection Postman này cung cấp đầy đủ các test case để kiểm tra tính năng Chat của JobHunter. Đảm bảo:

1. Server Spring Boot đang chạy
2. Database có dữ liệu test
3. Import collection vào Postman
4. Chạy từng test case theo thứ tự

Nếu có vấn đề, hãy kiểm tra logs và database connection trước. 