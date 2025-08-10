#!/bin/bash

# Test API cho Favorite Jobs
BASE_URL="http://localhost:8080/api/favorite-jobs"

echo "=== Test API Favorite Jobs ==="

# Test 1: Lấy danh sách job yêu thích
echo "1. Lấy danh sách job yêu thích của user 1:"
curl -X GET "$BASE_URL?userId=1" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Test 2: Thêm job vào danh sách yêu thích
echo "2. Thêm job 1 vào danh sách yêu thích của user 1:"
curl -X POST "$BASE_URL/add?userId=1&jobId=1" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Test 3: Kiểm tra job có được yêu thích không
echo "3. Kiểm tra job 1 có được yêu thích bởi user 1:"
curl -X GET "$BASE_URL/check?userId=1&jobId=1" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Test 4: Đếm số job yêu thích
echo "4. Đếm số job yêu thích của user 1:"
curl -X GET "$BASE_URL/count?userId=1" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Test 5: Xóa job khỏi danh sách yêu thích
echo "5. Xóa job 1 khỏi danh sách yêu thích của user 1:"
curl -X DELETE "$BASE_URL/remove?userId=1&jobId=1" \
  -H "Content-Type: application/json"

echo -e "\n\n"

# Test 6: Kiểm tra lại sau khi xóa
echo "6. Kiểm tra lại job 1 sau khi xóa:"
curl -X GET "$BASE_URL/check?userId=1&jobId=1" \
  -H "Content-Type: application/json"

echo -e "\n\n=== Test hoàn thành ===" 