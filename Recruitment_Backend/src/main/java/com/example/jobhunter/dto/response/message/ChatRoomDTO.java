package com.example.jobhunter.dto.response.message;

import java.time.Instant;
import lombok.Data;

@Data
public class ChatRoomDTO {
    private String id;
    private Long participantId; // Thêm trường này để Flutter biết ID người còn lại
    private String participantName;
    private String participantPhotoUrl;
    private String lastMessage;
    private Instant lastMessageTimestamp;
}
