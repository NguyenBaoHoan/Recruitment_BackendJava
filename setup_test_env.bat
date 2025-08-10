@echo off
echo ========================================
echo    Setup JobHunter Test Environment
echo ========================================
echo.

echo Đang cài đặt Python dependencies...
pip install -r requirements.txt

if %errorlevel% neq 0 (
    echo Lỗi cài đặt Python dependencies!
    echo Thử cài đặt thủ công:
    echo pip install websockets requests
    pause
    exit /b 1
)

echo.
echo ✅ Đã cài đặt thành công Python dependencies!
echo.
echo Các file test đã sẵn sàng:
echo - test_chat_powershell.ps1 (PowerShell test)
echo - test_chat_curl.sh (cURL test)
echo - test_websocket_simple.py (WebSocket test)
echo - test_websocket_chat.py (Multi-client WebSocket test)
echo.
echo Chạy run_tests.bat để bắt đầu test!
echo.
pause 