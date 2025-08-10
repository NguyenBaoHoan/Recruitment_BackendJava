#!/usr/bin/env python3
"""
Script test WebSocket chat cho JobHunter Backend
Sử dụng: python test_websocket_chat.py
"""

import asyncio
import websockets
import json
import time
import threading
from datetime import datetime

# Cấu hình
WS_URL = "ws://localhost:8080/ws"
HTTP_URL = "http://localhost:8080"

class ChatClient:
    def __init__(self, client_id, user_id, username):
        self.client_id = client_id
        self.user_id = user_id
        self.username = username
        self.websocket = None
        self.connected = False
        
    async def connect(self):
        try:
            self.websocket = await websockets.connect(WS_URL)
            self.connected = True
            print(f"[{self.client_id}] Đã kết nối WebSocket")
            
            # Subscribe vào topic chung
            subscribe_message = {
                "destination": "/topic/rooms/general",
                "id": f"sub-{self.client_id}"
            }
            await self.websocket.send(json.dumps(subscribe_message))
            print(f"[{self.client_id}] Đã subscribe vào /topic/rooms/general")
            
        except Exception as e:
            print(f"[{self.client_id}] Lỗi kết nối: {e}")
            self.connected = False
            
    async def send_message(self, content, recipient_id=None):
        if not self.connected:
            print(f"[{self.client_id}] Chưa kết nối!")
            return
            
        message = {
            "destination": "/app/chat.sendMessage",
            "content": json.dumps({
                "content": content,
                "senderName": self.username,
                "senderId": self.user_id,
                "recipientId": recipient_id or 2,  # Default recipient
                "messageType": "TEXT"
            })
        }
        
        try:
            await self.websocket.send(json.dumps(message))
            print(f"[{self.client_id}] Đã gửi: {content}")
        except Exception as e:
            print(f"[{self.client_id}] Lỗi gửi tin nhắn: {e}")
            
    async def listen(self):
        if not self.connected:
            return
            
        try:
            async for message in self.websocket:
                try:
                    data = json.loads(message)
                    if "payload" in data:
                        payload = json.loads(data["payload"])
                        if "content" in payload:
                            print(f"[{self.client_id}] Nhận: {payload['senderName']}: {payload['content']}")
                    else:
                        print(f"[{self.client_id}] Nhận: {data}")
                except json.JSONDecodeError:
                    print(f"[{self.client_id}] Nhận raw: {message}")
        except Exception as e:
            print(f"[{self.client_id}] Lỗi nhận tin nhắn: {e}")
            
    async def disconnect(self):
        if self.websocket:
            await self.websocket.close()
            self.connected = False
            print(f"[{self.client_id}] Đã ngắt kết nối")

async def test_chat():
    # Tạo 3 client để test
    clients = [
        ChatClient("Client1", 1, "Alice"),
        ChatClient("Client2", 2, "Bob"),
        ChatClient("Client3", 3, "Charlie")
    ]
    
    # Kết nối tất cả clients
    print("Đang kết nối các clients...")
    for client in clients:
        await client.connect()
        await asyncio.sleep(0.5)
    
    # Bắt đầu lắng nghe tin nhắn
    listen_tasks = [asyncio.create_task(client.listen()) for client in clients]
    
    # Test gửi tin nhắn
    print("\n=== Bắt đầu test chat ===\n")
    
    # Client 1 gửi tin nhắn
    await asyncio.sleep(1)
    await clients[0].send_message("Xin chào mọi người!", 2)
    
    await asyncio.sleep(2)
    await clients[1].send_message("Chào Alice! Rất vui được gặp bạn!", 1)
    
    await asyncio.sleep(2)
    await clients[2].send_message("Chào cả nhà! Tôi là Charlie", 1)
    
    await asyncio.sleep(2)
    await clients[0].send_message("Cảm ơn mọi người đã tham gia chat!", 2)
    
    # Chờ một chút để nhận tin nhắn
    await asyncio.sleep(5)
    
    # Ngắt kết nối
    print("\n=== Kết thúc test ===\n")
    for client in clients:
        await client.disconnect()
    
    # Hủy các task lắng nghe
    for task in listen_tasks:
        task.cancel()

def test_http_endpoints():
    """Test các HTTP endpoints"""
    import requests
    
    print("=== Test HTTP Endpoints ===")
    
    # Test gửi tin nhắn qua HTTP
    try:
        payload = {
            "content": "Test message via HTTP",
            "senderName": "TestUser",
            "senderId": 1,
            "recipientId": 2,
            "messageType": "TEXT"
        }
        
        response = requests.post(f"{HTTP_URL}/chat/sendMessage", json=payload)
        print(f"HTTP POST /chat/sendMessage: {response.status_code}")
        if response.status_code == 200:
            print(f"Response: {response.json()}")
    except Exception as e:
        print(f"Lỗi HTTP test: {e}")

if __name__ == "__main__":
    print("=== JobHunter WebSocket Chat Test ===")
    print("Đảm bảo server đang chạy trên localhost:8080")
    print("Nhấn Ctrl+C để dừng\n")
    
    # Test HTTP endpoints trước
    test_http_endpoints()
    print()
    
    # Test WebSocket
    try:
        asyncio.run(test_chat())
    except KeyboardInterrupt:
        print("\nĐã dừng test bởi người dùng")
    except Exception as e:
        print(f"Lỗi: {e}") 