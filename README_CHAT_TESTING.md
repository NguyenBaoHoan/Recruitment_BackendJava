# Hướng dẫn Test Realtime Chat cho JobHunter Backend

## Yêu cầu

1. **Server đang chạy**: Đảm bảo Spring Boot server đang chạy trên `localhost:8080`
2. **Python**: Cài đặt Python 3.7+ và thư viện `websockets`
3. **cURL**: Để test HTTP endpoints

## Cài đặt dependencies

```bash
# Cài đặt websockets cho Python
pip install websockets requests

# Hoặc nếu dùng pip3
pip3 install websockets requests
```

## Các cách test

### 1. Test HTTP Endpoints (Đơn giản nhất)

```bash
# Chạy script bash
bash test_chat_curl.sh

# Hoặc test từng lệnh riêng lẻ
curl -X POST "http://localhost:8080/chat/sendMessage" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Test message",
    "senderName": "Alice",
    "senderId": 1,
    "recipientId": 2,
    "messageType": "TEXT"
  }'
```

### 2. Test WebSocket (Realtime)

```bash
# Test WebSocket đơn giản
python test_websocket_simple.py

# Test WebSocket với nhiều client
python test_websocket_chat.py
```

### 3. Test bằng wscat (nếu có)

```bash
# Cài đặt wscat
npm install -g wscat

# Kết nối WebSocket
wscat -c ws://localhost:8080/ws

# Subscribe vào topic
{"destination": "/topic/rooms/general", "id": "sub-1"}

# Gửi tin nhắn
{"destination": "/app/chat.sendMessage", "content": "{\"content\":\"Test\",\"senderName\":\"User\",\"senderId\":1,\"recipientId\":2,\"messageType\":\"TEXT\"}"}
```

## Cấu trúc WebSocket

### Endpoints
- **WebSocket**: `ws://localhost:8080/ws`
- **HTTP**: `http://localhost:8080/chat/sendMessage`

### Topics
- **Public**: `/topic/rooms/{roomId}`
- **Private**: `/user/{userId}/queue/messages`
- **Errors**: `/user/{userId}/queue/errors`
- **Notifications**: `/user/{userId}/queue/notifications`

### Message Format

#### Gửi tin nhắn qua WebSocket:
```json
{
  "destination": "/app/chat.sendMessage",
  "content": {
    "content": "Nội dung tin nhắn",
    "senderName": "Tên người gửi",
    "senderId": 1,
    "recipientId": 2,
    "messageType": "TEXT"
  }
}
```

#### Gửi tin nhắn qua HTTP:
```json
{
  "content": "Nội dung tin nhắn",
  "senderName": "Tên người gửi",
  "senderId": 1,
  "recipientId": 2,
  "messageType": "TEXT"
}
```

## Kiểm tra kết quả

### Thành công:
- ✅ Kết nối WebSocket thành công
- ✅ Gửi tin nhắn không bị lỗi
- ✅ Nhận được tin nhắn từ server
- ✅ HTTP endpoints trả về status 200

### Lỗi thường gặp:
- ❌ "Connection refused": Server chưa chạy
- ❌ "404 Not Found": Endpoint không đúng
- ❌ "400 Bad Request": Payload không đúng format
- ❌ "401 Unauthorized": Chưa authenticate

## Debug

### Kiểm tra server logs:
```bash
# Xem logs của Spring Boot
tail -f logs/application.log
```

### Test database:
```sql
-- Kiểm tra tin nhắn đã được lưu
SELECT * FROM messages ORDER BY created_at DESC LIMIT 10;

-- Kiểm tra phòng chat
SELECT * FROM chat_rooms;
```

## Lưu ý

1. **User IDs**: Đảm bảo các user ID (1, 2, 3) đã tồn tại trong database
2. **CORS**: WebSocket đã được cấu hình cho phép tất cả origins
3. **Security**: Hiện tại tất cả endpoints đều `permitAll()` để test dễ dàng
4. **Database**: Cần có dữ liệu users trong database để test

## Troubleshooting

### Server không start:
- Kiểm tra port 8080 có bị chiếm không
- Kiểm tra database connection
- Xem logs lỗi trong console

### WebSocket không kết nối:
- Kiểm tra endpoint `/ws` có đúng không
- Kiểm tra CORS configuration
- Thử test với wscat trước

### Tin nhắn không được gửi:
- Kiểm tra format JSON
- Kiểm tra senderId và recipientId có tồn tại
- Xem logs server để debug 