#!/bin/bash

# Script test chat cho JobHunter Backend
# Sử dụng: bash test_chat_curl.sh

echo "=== JobHunter Chat Test với cURL ==="
echo "Đảm bảo server đang chạy trên localhost:8080"
echo ""

BASE_URL="http://localhost:8080"

# Test 1: Gửi tin nhắn từ Alice đến Bob
echo "Test 1: Alice gửi tin nhắn cho Bob"
curl -X POST "$BASE_URL/chat/sendMessage" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Xin chào Bob! Đây là tin nhắn test từ Alice",
    "senderName": "Alice",
    "senderId": 1,
    "recipientId": 2,
    "messageType": "TEXT"
  }'
echo -e "\n"

# Test 2: Bob trả lời Alice
echo "Test 2: Bob trả lời Alice"
curl -X POST "$BASE_URL/chat/sendMessage" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Chào Alice! Rất vui được gặp bạn",
    "senderName": "Bob",
    "senderId": 2,
    "recipientId": 1,
    "messageType": "TEXT"
  }'
echo -e "\n"

# Test 3: Charlie gửi tin nhắn cho Alice
echo "Test 3: Charlie gửi tin nhắn cho Alice"
curl -X POST "$BASE_URL/chat/sendMessage" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Chào Alice! Tôi là Charlie, rất vui được làm quen",
    "senderName": "Charlie",
    "senderId": 3,
    "recipientId": 1,
    "messageType": "TEXT"
  }'
echo -e "\n"

# Test 4: Lấy lịch sử tin nhắn (nếu có roomId)
echo "Test 4: Lấy lịch sử tin nhắn (cần roomId hợp lệ)"
curl -X GET "$BASE_URL/chat/rooms/1/messages" \
  -H "Content-Type: application/json"
echo -e "\n"

# Test 5: Lấy danh sách phòng chat của user
echo "Test 5: Lấy danh sách phòng chat của user"
curl -X GET "$BASE_URL/chat/users/1/rooms" \
  -H "Content-Type: application/json"
echo -e "\n"

echo "=== Kết thúc test ===" 