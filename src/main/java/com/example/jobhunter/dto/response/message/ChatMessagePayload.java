package com.example.jobhunter.dto.response.message;

import com.example.jobhunter.util.constant.MessageType;
import lombok.Data;

/**
 * Payload nhận từ client qua WebSocket khi gửi tin nhắn.
 */
@Data
public class ChatMessagePayload {
    private String content;
    private String senderName; // Tên người gửi, có thể là username hoặc tên hiển thị
    private Long senderId;
    private Long recipientId; // Cần ID người nhận để tìm hoặc tạo phòng chat
    private MessageType messageType = MessageType.TEXT; // SỬA LỖI: Đã bỏ tiền tố "Message."
}
