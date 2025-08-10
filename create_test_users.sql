-- Script tạo dữ liệu test cho JobHunter Chat
-- Chạy: mysql -u root -p jobhunter < create_test_users.sql

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM messages WHERE id > 0;
DELETE FROM chat_rooms WHERE id > 0;
DELETE FROM users WHERE id IN (1, 2, 3);

-- Tạo users test
INSERT INTO users (id, name, email, password, gender, address, age, created_at, updated_at) VALUES
(1, 'Alice Johnson', 'alice@test.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'FEMALE', '123 Main St, City', 25, NOW(), NOW()),
(2, 'Bob Smith', 'bob@test.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MALE', '456 Oak Ave, Town', 30, NOW(), NOW()),
(3, 'Charlie Brown', 'charlie@test.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MALE', '789 Pine Rd, Village', 28, NOW(), NOW());

-- Tạo chat rooms test
INSERT INTO chat_rooms (id, name, created_at, updated_at) VALUES
(1, 'Alice-Bob Chat', NOW(), NOW()),
(2, 'Alice-Charlie Chat', NOW(), NOW()),
(3, 'Bob-Charlie Chat', NOW(), NOW());

-- Tạo messages test
INSERT INTO messages (id, content, sender_id, recipient_id, chat_room_id, message_type, created_at, updated_at) VALUES
(1, 'Xin chào! Đây là tin nhắn test', 1, 2, 1, 'TEXT', NOW(), NOW()),
(2, 'Chào bạn! Rất vui được gặp', 2, 1, 1, 'TEXT', NOW(), NOW()),
(3, 'Test message từ Charlie', 3, 1, 2, 'TEXT', NOW(), NOW());

-- Hiển thị kết quả
SELECT 'Users created:' as info;
SELECT id, name, email FROM users WHERE id IN (1, 2, 3);

SELECT 'Chat rooms created:' as info;
SELECT * FROM chat_rooms;

SELECT 'Messages created:' as info;
SELECT * FROM messages; 