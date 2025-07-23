package com.example.jobhunter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobhunter.domain.ChatRoom;
import com.example.jobhunter.domain.Message;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.dto.response.message.ChatMessagePayload;
import com.example.jobhunter.dto.response.message.ChatRoomDTO;
import com.example.jobhunter.dto.response.message.MessageDTO;
import com.example.jobhunter.repository.ChatRoomRepository;
import com.example.jobhunter.repository.MessageRepository;
import com.example.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection.
     * Spring sẽ tự động "tiêm" các dependency cần thiết vào constructor này.
     */
    public ChatService(MessageRepository messageRepository,
            ChatRoomRepository chatRoomRepository,
            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lấy lịch sử tin nhắn của một phòng chat.
     */
    public List<MessageDTO> findMessagesForRoom(String chatRoomId) {
        List<Message> messages = messageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
        // Chuyển đổi từ Entity sang DTO
        return messages.stream().map(this::convertToMessageDTO).collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các phòng chat của một người dùng.
     */
    public List<ChatRoomDTO> findRoomsForUser(Long userId) {
        System.out.println("=== DEBUG: Finding rooms for user: " + userId + " ===");
        List<ChatRoom> chatRooms = chatRoomRepository.findByParticipants_Id(userId);
        System.out.println("Found " + chatRooms.size() + " chat rooms");

        return chatRooms.stream().map(room -> {
            System.out.println("Processing room ID: " + room.getId());
            System.out.println("Room participants count: " + room.getParticipants().size());
            System.out.println("Room participants IDs: " + room.getParticipants().stream()
                    .map(p -> String.valueOf(p.getId()))
                    .collect(Collectors.joining(", ")));

            ChatRoomDTO dto = new ChatRoomDTO();
            dto.setId(room.getId());

            // Tìm ID của người còn lại trong phòng chat
            Long otherParticipantId = room.getParticipants().stream()
                    .map(User::getId)
                    .filter(id -> !id.equals(userId))
                    .findFirst()
                    .orElse(null);

            System.out.println("Other participant ID found: " + otherParticipantId);
            dto.setParticipantId(otherParticipantId);

            // Lấy thông tin người còn lại
            if (otherParticipantId != null) {
                User otherParticipant = userRepository.findById(otherParticipantId).orElse(null);
                if (otherParticipant != null) {
                    dto.setParticipantName(otherParticipant.getDisplayName());
                    dto.setParticipantPhotoUrl(otherParticipant.getPhotoUrl());
                    System.out.println("Other participant name: " + otherParticipant.getDisplayName());
                }
            }

            // Lấy tin nhắn cuối cùng
            List<Message> messages = messageRepository.findByChatRoomIdOrderByCreatedAtDesc(room.getId());
            if (!messages.isEmpty()) {
                Message lastMessage = messages.get(0);
                dto.setLastMessage(lastMessage.getContent());
                dto.setLastMessageTimestamp(lastMessage.getCreatedAt());
            }

            System.out.println("Final DTO participantId: " + dto.getParticipantId());
            System.out.println("=== END DEBUG ===");

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Xử lý và lưu tin nhắn mới.
     * 
     * @return MessageDTO của tin nhắn đã được lưu.
     */
    @Transactional
    public MessageDTO processAndSaveMessage(ChatMessagePayload payload) {
        // Tìm hoặc tạo phòng chat
        String chatRoomId = getChatRoomId(payload.getSenderId(), payload.getRecipientId(), true)
                .orElseThrow(() -> new RuntimeException("Could not create or find chat room"));

        // Tìm người gửi
        User sender = userRepository.findById(payload.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        // Tạo và lưu tin nhắn
        Message message = new Message();
        message.setSenderName(payload.getSenderName());
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(payload.getContent());
        message.setMessageType(payload.getMessageType());

        Message savedMessage = messageRepository.save(message);

        // Cập nhật trường last_updated của phòng chat (tùy chọn)
        chatRoom.setLastUpdated(savedMessage.getCreatedAt());
        chatRoomRepository.save(chatRoom);

        return convertToMessageDTO(savedMessage);
    }

    /**
     * Tìm hoặc tạo ID phòng chat giữa hai người dùng.
     */
    private Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createIfNotExists) {
        // Sắp xếp ID để đảm bảo ID phòng chat là duy nhất và không đổi
        Long id1 = Math.min(senderId, recipientId);
        Long id2 = Math.max(senderId, recipientId);
        String roomId = String.format("%d_%d", id1, id2);

        return chatRoomRepository.findById(roomId)
                .map(ChatRoom::getId)
                .or(() -> {
                    if (!createIfNotExists) {
                        return Optional.empty();
                    }
                    // Nếu phòng chat chưa tồn tại, tạo mới
                    User sender = userRepository.findById(senderId).orElseThrow();
                    User recipient = userRepository.findById(recipientId).orElseThrow();

                    ChatRoom newChatRoom = new ChatRoom(roomId);
                    newChatRoom.getParticipants().add(sender);
                    newChatRoom.getParticipants().add(recipient);
                    chatRoomRepository.save(newChatRoom);

                    return Optional.of(roomId);
                });
    }

    // Hàm tiện ích để chuyển đổi Message Entity sang MessageDTO
    private MessageDTO convertToMessageDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setChatRoomId(message.getChatRoom().getId());
        dto.setContent(message.getContent());
        dto.setSenderName(message.getSenderName());
        dto.setTimestamp(message.getCreatedAt());
        dto.setMessageType(message.getMessageType());
        if (message.getSender() != null) {
            dto.setSenderId(message.getSender().getId());
            dto.setSenderDisplayName(message.getSender().getDisplayName());
        }
        return dto;
    }
}
