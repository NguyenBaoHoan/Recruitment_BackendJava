package com.example.jobhunter.dto.response.message;

import java.time.Instant;
import lombok.Data;

@Data
public class ChatRoomDTO {
    private String id;
    private String otherParticipantName;
    private String otherParticipantPhotoUrl;
    private String lastMessage;
    private Instant lastMessageTimestamp;
}
