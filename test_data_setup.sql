-- SQL Script để chuẩn bị dữ liệu test cho Chat API
-- Chạy script này trước khi test các API Chat

-- 1. Tạo users test (nếu chưa có)
INSERT INTO users (id, display_name, email, password, role, created_at, updated_at) VALUES 
(1, 'John Doe', 'john@example.com', '$2a$10$example', 'USER', NOW(), NOW()),
(2, 'Jane Smith', 'jane@example.com', '$2a$10$example', 'USER', NOW(), NOW()),
(3, 'Bob Wilson', 'bob@example.com', '$2a$10$example', 'USER', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
display_name = VALUES(display_name),
email = VALUES(email);

-- 2. Tạo phòng chat test
INSERT INTO chat_rooms (id, last_updated) VALUES 
('1_2', NOW()),
('1_3', NOW()),
('2_3', NOW())
ON DUPLICATE KEY UPDATE 
last_updated = NOW();

-- 3. Thêm participants vào phòng chat
INSERT INTO chat_room_participants (chat_room_id, user_id) VALUES 
('1_2', 1),
('1_2', 2),
('1_3', 1),
('1_3', 3),
('2_3', 2),
('2_3', 3)
ON DUPLICATE KEY UPDATE 
chat_room_id = VALUES(chat_room_id);

-- 4. Tạo một số tin nhắn test
INSERT INTO messages (content, created_at, message_type, is_read, chat_room_id, sender_id) VALUES 
-- Tin nhắn trong phòng 1_2
('Hello Jane! How are you?', DATE_SUB(NOW(), INTERVAL 1 HOUR), 'TEXT', false, '1_2', 1),
('Hi John! I am fine, thank you!', DATE_SUB(NOW(), INTERVAL 30 MINUTE), 'TEXT', false, '1_2', 2),
('Great! Do you want to grab coffee?', DATE_SUB(NOW(), INTERVAL 15 MINUTE), 'TEXT', false, '1_2', 1),

-- Tin nhắn trong phòng 1_3
('Hey Bob!', DATE_SUB(NOW(), INTERVAL 2 HOUR), 'TEXT', false, '1_3', 1),
('Hi John!', DATE_SUB(NOW(), INTERVAL 1 HOUR), 'TEXT', false, '1_3', 3),

-- Tin nhắn trong phòng 2_3
('Hello Bob!', DATE_SUB(NOW(), INTERVAL 3 HOUR), 'TEXT', false, '2_3', 2),
('Hi Jane!', DATE_SUB(NOW(), INTERVAL 2 HOUR), 'TEXT', false, '2_3', 3),

-- Tin nhắn với các loại khác nhau
('https://example.com/image1.jpg', DATE_SUB(NOW(), INTERVAL 10 MINUTE), 'IMAGE', false, '1_2', 1),
('https://example.com/audio1.mp3', DATE_SUB(NOW(), INTERVAL 5 MINUTE), 'AUDIO', false, '1_2', 2),
('Check out this file!', DATE_SUB(NOW(), INTERVAL 2 MINUTE), 'FILE', false, '1_3', 1);

-- 5. Kiểm tra dữ liệu đã được tạo
SELECT 'Users:' as info;
SELECT id, display_name, email FROM users WHERE id IN (1,2,3);

SELECT 'Chat Rooms:' as info;
SELECT id, last_updated FROM chat_rooms WHERE id IN ('1_2', '1_3', '2_3');

SELECT 'Chat Room Participants:' as info;
SELECT crp.chat_room_id, u.display_name 
FROM chat_room_participants crp 
JOIN users u ON crp.user_id = u.id 
WHERE crp.chat_room_id IN ('1_2', '1_3', '2_3')
ORDER BY crp.chat_room_id, u.display_name;

SELECT 'Messages:' as info;
SELECT m.id, m.content, m.message_type, m.created_at, 
       u.display_name as sender, m.chat_room_id
FROM messages m 
JOIN users u ON m.sender_id = u.id 
WHERE m.chat_room_id IN ('1_2', '1_3', '2_3')
ORDER BY m.chat_room_id, m.created_at DESC; 