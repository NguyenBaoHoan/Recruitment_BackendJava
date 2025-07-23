package com.example.jobhunter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.dto.response.message.ChatMessagePayload;
import com.example.jobhunter.dto.response.message.ChatRoomDTO;
import com.example.jobhunter.dto.response.message.MessageDTO;
import com.example.jobhunter.service.ChatService;
import com.example.jobhunter.service.UserService;

import java.util.List;

@RestController
public class ChatController {

    private ChatService chatService;
    private UserService userService;
    private SimpMessageSendingOperations messagingTemplate;

    public ChatController(ChatService chatService, UserService userService,
            SimpMessageSendingOperations messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Endpoint REST để lấy lịch sử tin nhắn của một phòng chat.
     */
    @GetMapping("/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable("roomId") String roomId) {
        try {
            // Kiểm tra user hiện tại có quyền xem tin nhắn trong room này không
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }

            List<MessageDTO> messages = chatService.findMessagesForRoom(roomId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint REST để lấy danh sách các phòng chat của một người dùng.
     */
    @GetMapping("/chat/users/{userId}/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms(@PathVariable("userId") Long userId) {
        try {
            // Kiểm tra user hiện tại có quyền xem danh sách room của userId không
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }

            List<ChatRoomDTO> rooms = chatService.findRoomsForUser(userId);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint WebSocket để xử lý tin nhắn gửi đến.
     * Client sẽ gửi tin nhắn tới destination "/app/chat.sendMessage".
     */
    @MessageMapping("/chat.sendMessage")
    @SendToUser("/queue/errors")
    public void sendMessage(@Payload ChatMessagePayload payload) {
        try {
            // Validate payload
            if (payload.getContent() == null || payload.getContent().trim().isEmpty()) {
                messagingTemplate.convertAndSendToUser(
                        payload.getSenderId().toString(),
                        "/queue/errors",
                        "Message content cannot be empty");
                return;
            }

            if (payload.getSenderId() == null || payload.getRecipientId() == null) {
                messagingTemplate.convertAndSendToUser(
                        payload.getSenderId() != null ? payload.getSenderId().toString() : "unknown",
                        "/queue/errors",
                        "Sender ID and Recipient ID are required");
                return;
            }

            // Kiểm tra sender có tồn tại không
            if (!userService.existsById(payload.getSenderId())) {
                messagingTemplate.convertAndSendToUser(
                        payload.getSenderId().toString(),
                        "/queue/errors",
                        "Sender not found");
                return;
            }

            // Kiểm tra recipient có tồn tại không
            if (!userService.existsById(payload.getRecipientId())) {
                messagingTemplate.convertAndSendToUser(
                        payload.getSenderId().toString(),
                        "/queue/errors",
                        "Recipient not found");
                return;
            }

            // 1. Lưu tin nhắn vào CSDL
            MessageDTO savedMessage = chatService.processAndSaveMessage(payload);

            // 2. Gửi tin nhắn tới tất cả client đang lắng nghe trên topic của phòng chat đó
            String chatRoomId = savedMessage.getChatRoomId();
            messagingTemplate.convertAndSend("/topic/rooms/" + chatRoomId, savedMessage);

            // 3. Gửi notification cho recipient nếu họ không online
            messagingTemplate.convertAndSendToUser(
                    payload.getRecipientId().toString(),
                    "/queue/notifications",
                    "You have a new message from " + savedMessage.getSenderName());

        } catch (Exception e) {
            // Gửi error message về cho sender
            messagingTemplate.convertAndSendToUser(
                    payload.getSenderId().toString(),
                    "/queue/errors",
                    "Failed to send message: " + e.getMessage());
        }
    }

    /*
     * Endpoint REST để test gửi tin nhắn (cho mục đích testing).
     * Trong production, nên sử dụng WebSocket endpoint.
     */
    @PostMapping("/chat/sendMessage")
    public ResponseEntity<MessageDTO> sendMessageRest(@RequestBody ChatMessagePayload payload) {
        try {
            // Validate payload
            if (payload.getContent() == null || payload.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            if (payload.getSenderId() == null || payload.getRecipientId() == null) {
                return ResponseEntity.badRequest().build();
            }

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
