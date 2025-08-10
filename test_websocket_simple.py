#!/usr/bin/env python3
"""
Script test WebSocket đơn giản cho JobHunter Backend
Sử dụng: python test_websocket_simple.py
"""

import asyncio
import websockets
import json
import time

WS_URL = "ws://localhost:8080/ws"

async def test_websocket():
    print("=== Test WebSocket Connection ===")
    
    try:
        # Kết nối WebSocket
        print("Đang kết nối đến WebSocket...")
        websocket = await websockets.connect(WS_URL)
        print("✅ Đã kết nối thành công!")
        
        # Subscribe vào topic
        subscribe_msg = {
            "destination": "/topic/rooms/general",
            "id": "sub-1"
        }
        await websocket.send(json.dumps(subscribe_msg))
        print("✅ Đã subscribe vào /topic/rooms/general")
        
        # Gửi tin nhắn test
        test_message = {
            "destination": "/app/chat.sendMessage",
            "content": json.dumps({
                "content": "Test message từ WebSocket client",
                "senderName": "TestUser",
                "senderId": 1,
                "recipientId": 2,
                "messageType": "TEXT"
            })
        }
        
        print("Đang gửi tin nhắn test...")
        await websocket.send(json.dumps(test_message))
        print("✅ Đã gửi tin nhắn")
        
        # Lắng nghe tin nhắn trong 5 giây
        print("Đang lắng nghe tin nhắn (5 giây)...")
        start_time = time.time()
        
        while time.time() - start_time < 5:
            try:
                message = await asyncio.wait_for(websocket.recv(), timeout=1.0)
                print(f"📨 Nhận: {message}")
            except asyncio.TimeoutError:
                continue
            except Exception as e:
                print(f"❌ Lỗi nhận tin nhắn: {e}")
                break
        
        # Đóng kết nối
        await websocket.close()
        print("✅ Đã đóng kết nối")
        
    except Exception as e:
        print(f"❌ Lỗi: {e}")

if __name__ == "__main__":
    print("JobHunter WebSocket Test")
    print("Đảm bảo server đang chạy trên localhost:8080")
    print()
    
    try:
        asyncio.run(test_websocket())
    except KeyboardInterrupt:
        print("\nĐã dừng bởi người dùng")
    except Exception as e:
        print(f"Lỗi: {e}") 