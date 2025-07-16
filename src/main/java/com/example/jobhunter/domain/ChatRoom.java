package com.example.jobhunter.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.jobhunter.util.error.SecurityUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "chat_rooms")
@Data
public class ChatRoom {
    @Id
    
    private String id; // Sử dụng String ID thay vì auto-generated

    private Instant lastUpdated;

    @ManyToMany
    @JoinTable(name = "chat_room_participants", joinColumns = @JoinColumn(name = "chat_room_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants = new ArrayList<>();

    // Constructor để tạo ChatRoom với ID
    public ChatRoom(String id) {
        this.id = id;
        this.lastUpdated = Instant.now();
    }

    // Default constructor cho JPA
    public ChatRoom() {
        this.lastUpdated = Instant.now();
    }

    @PreUpdate
    public void handlelastUpdated() {
        this.lastUpdated = Instant.now();
    }
}
