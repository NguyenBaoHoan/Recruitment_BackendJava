package com.example.jobhunter.dto.response.message;

import java.time.Instant;

import com.example.jobhunter.domain.Message;
import com.example.jobhunter.util.constant.MessageType;

import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private String chatRoomId;
    private Long senderId;
    private String senderDisplayName;
    private String content;
    private Instant timestamp;
    private MessageType messageType;
}
