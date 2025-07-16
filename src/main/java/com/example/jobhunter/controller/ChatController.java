package com.example.jobhunter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.dto.response.message.ChatMessagePayload;
import com.example.jobhunter.dto.response.message.ChatRoomDTO;
import com.example.jobhunter.dto.response.message.MessageDTO;
import com.example.jobhunter.service.ChatService;

import java.util.List;

@RestController
public class ChatController {

    private ChatService chatService;

    private SimpMessageSendingOperations messagingTemplate; // Bean để gửi tin nhắn qua WebSocket

    public ChatController(ChatService chatService, SimpMessageSendingOperations messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Endpoint REST để lấy lịch sử tin nhắn của một phòng chat.
     */
    @GetMapping("/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(chatService.findMessagesForRoom(roomId));
    }

    /**
     * Endpoint REST để lấy danh sách các phòng chat của một người dùng.
     */
    @GetMapping("/chat/users/{userId}/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatService.findRoomsForUser(userId));
    }

    /**
     * Endpoint WebSocket để xử lý tin nhắn gửi đến.
     * Client sẽ gửi tin nhắn tới destination "/app/chat.sendMessage".
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessagePayload payload) {
        // 1. Lưu tin nhắn vào CSDL
        MessageDTO savedMessage = chatService.processAndSaveMessage(payload);

        // 2. Gửi tin nhắn tới tất cả client đang lắng nghe trên topic của phòng chat đó
        // Ví dụ: /topic/rooms/1_5
        String chatRoomId = savedMessage.getChatRoomId();
        messagingTemplate.convertAndSend("/topic/rooms/" + chatRoomId, savedMessage);
    }

    /**
     * Endpoint REST để test gửi tin nhắn (cho mục đích testing).
     * Trong production, nên sử dụng WebSocket endpoint.
     */
    @PostMapping("/chat/sendMessage")
    public ResponseEntity<MessageDTO> sendMessageRest(@RequestBody ChatMessagePayload payload) {
        try {
            MessageDTO savedMessage = chatService.processAndSaveMessage(payload);

            // Gửi tin nhắn qua WebSocket nếu có client đang lắng nghe
            String chatRoomId = savedMessage.getChatRoomId();
            messagingTemplate.convertAndSend("/topic/rooms/" + chatRoomId, savedMessage);

            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
