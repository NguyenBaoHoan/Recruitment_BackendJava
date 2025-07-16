# Chat API Testing - JobHunter

## Quick Start

### 1. Chuẩn Bị Dữ Liệu
```bash
# Chạy SQL script để tạo dữ liệu test
mysql -u root -p jobhunter < test_data_setup.sql
```

### 2. Import Postman Collection
1. Mở Postman
2. Import file: `Chat_API_Tests.postman_collection.json`
3. Collection sẽ có sẵn các test cases

### 3. Test APIs
Chạy các test theo thứ tự:
1. **Get Chat History** - Lấy tin nhắn trong phòng chat
2. **Get User Chat Rooms** - Lấy danh sách phòng chat
3. **Send Message** - Gửi tin nhắn mới
4. **Test Message Types** - Test các loại tin nhắn khác

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/chat/rooms/{roomId}/messages` | Lấy lịch sử tin nhắn |
| GET | `/chat/users/{userId}/rooms` | Lấy danh sách phòng chat |
| POST | `/chat/send-message` | Gửi tin nhắn (REST) |
| WS | `/app/chat.sendMessage` | Gửi tin nhắn (WebSocket) |

## Message Types
- `TEXT` - Tin nhắn văn bản
- `IMAGE` - Tin nhắn hình ảnh  
- `AUDIO` - Tin nhắn âm thanh
- `VIDEO` - Tin nhắn video
- `FILE` - Tin nhắn file
- `LOCATION` - Tin nhắn vị trí
- `OTHER` - Loại khác

## Files Included
- `Chat_API_Tests.postman_collection.json` - Postman collection
- `test_data_setup.sql` - SQL script tạo dữ liệu test
- `CHAT_API_TESTING_GUIDE.md` - Hướng dẫn chi tiết
- `README_CHAT_TESTING.md` - Hướng dẫn nhanh (file này)

## Troubleshooting
- Đảm bảo server Spring Boot đang chạy trên port 8080
- Kiểm tra database connection
- Xem logs nếu có lỗi
- Đảm bảo đã chạy SQL script trước khi test 