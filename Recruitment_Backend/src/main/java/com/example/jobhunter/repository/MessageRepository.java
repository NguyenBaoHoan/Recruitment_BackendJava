package com.example.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jobhunter.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Tìm tất cả tin nhắn trong một phòng chat, sắp xếp theo thời gian mới nhất.
     */
    List<Message> findByChatRoomIdOrderByCreatedAtDesc(String chatRoomId);
}
