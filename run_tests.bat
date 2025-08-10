@echo off
echo ========================================
echo    JobHunter Chat Testing Script
echo ========================================
echo.

echo Chọn loại test:
echo 1. Test HTTP endpoints với PowerShell
echo 2. Test HTTP endpoints với cURL
echo 3. Test WebSocket với Python
echo 4. Test tất cả
echo.

set /p choice="Nhập lựa chọn (1-4): "

if "%choice%"=="1" goto powershell_test
if "%choice%"=="2" goto curl_test
if "%choice%"=="3" goto python_test
if "%choice%"=="4" goto all_tests
goto invalid_choice

:powershell_test
echo.
echo === Test HTTP endpoints với PowerShell ===
powershell -ExecutionPolicy Bypass -File test_chat_powershell.ps1
goto end

:curl_test
echo.
echo === Test HTTP endpoints với cURL ===
bash test_chat_curl.sh
goto end

:python_test
echo.
echo === Test WebSocket với Python ===
python test_websocket_simple.py
goto end

:all_tests
echo.
echo === Chạy tất cả tests ===
echo.
echo 1. Test HTTP với PowerShell...
powershell -ExecutionPolicy Bypass -File test_chat_powershell.ps1
echo.
echo 2. Test HTTP với cURL...
bash test_chat_curl.sh
echo.
echo 3. Test WebSocket với Python...
python test_websocket_simple.py
goto end

:invalid_choice
echo Lựa chọn không hợp lệ!
goto end

:end
echo.
echo ========================================
echo Test hoàn thành!
echo ========================================
pause 