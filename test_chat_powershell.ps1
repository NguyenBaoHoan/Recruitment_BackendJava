# Script test chat cho JobHunter Backend trên Windows
# Sử dụng: .\test_chat_powershell.ps1

Write-Host "=== JobHunter Chat Test với PowerShell ===" -ForegroundColor Green
Write-Host "Đảm bảo server đang chạy trên localhost:8080" -ForegroundColor Yellow
Write-Host ""

$BASE_URL = "http://localhost:8080"

# Test 1: Alice gửi tin nhắn cho Bob
Write-Host "Test 1: Alice gửi tin nhắn cho Bob" -ForegroundColor Cyan
$payload1 = @{
    content = "Xin chào Bob! Đây là tin nhắn test từ Alice"
    senderName = "Alice"
    senderId = 1
    recipientId = 2
    messageType = "TEXT"
} | ConvertTo-Json

try {
    $response1 = Invoke-RestMethod -Uri "$BASE_URL/chat/sendMessage" -Method POST -Body $payload1 -ContentType "application/json"
    Write-Host "✅ Thành công: $($response1 | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "❌ Lỗi: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Bob trả lời Alice
Write-Host "Test 2: Bob trả lời Alice" -ForegroundColor Cyan
$payload2 = @{
    content = "Chào Alice! Rất vui được gặp bạn"
    senderName = "Bob"
    senderId = 2
    recipientId = 1
    messageType = "TEXT"
} | ConvertTo-Json

try {
    $response2 = Invoke-RestMethod -Uri "$BASE_URL/chat/sendMessage" -Method POST -Body $payload2 -ContentType "application/json"
    Write-Host "✅ Thành công: $($response2 | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "❌ Lỗi: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: Charlie gửi tin nhắn cho Alice
Write-Host "Test 3: Charlie gửi tin nhắn cho Alice" -ForegroundColor Cyan
$payload3 = @{
    content = "Chào Alice! Tôi là Charlie, rất vui được làm quen"
    senderName = "Charlie"
    senderId = 3
    recipientId = 1
    messageType = "TEXT"
} | ConvertTo-Json

try {
    $response3 = Invoke-RestMethod -Uri "$BASE_URL/chat/sendMessage" -Method POST -Body $payload3 -ContentType "application/json"
    Write-Host "✅ Thành công: $($response3 | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "❌ Lỗi: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Lấy lịch sử tin nhắn
Write-Host "Test 4: Lấy lịch sử tin nhắn" -ForegroundColor Cyan
try {
    $response4 = Invoke-RestMethod -Uri "$BASE_URL/chat/rooms/1/messages" -Method GET -ContentType "application/json"
    Write-Host "✅ Thành công: $($response4 | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "❌ Lỗi: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Lấy danh sách phòng chat
Write-Host "Test 5: Lấy danh sách phòng chat của user" -ForegroundColor Cyan
try {
    $response5 = Invoke-RestMethod -Uri "$BASE_URL/chat/users/1/rooms" -Method GET -ContentType "application/json"
    Write-Host "✅ Thành công: $($response5 | ConvertTo-Json)" -ForegroundColor Green
} catch {
    Write-Host "❌ Lỗi: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Write-Host "=== Kết thúc test ===" -ForegroundColor Green 