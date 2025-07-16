package com.example.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.jobhunter.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    /**
     * Tìm một phòng chat dựa trên ID của hai người tham gia.
     * 
     * @param userId1 ID người dùng 1
     * @param userId2 ID người dùng 2
     * @return Optional<ChatRoom>
     */
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p1 JOIN cr.participants p2 WHERE p1.id = :userId1 AND p2.id = :userId2")
    Optional<ChatRoom> findChatRoomByParticipants(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * Tìm tất cả các phòng chat mà một người dùng tham gia.
     */
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p WHERE p.id = :userId")
    List<ChatRoom> findByParticipants_Id(@Param("userId") Long userId);

    /**
     * Tìm ChatRoom bằng String roomId
     */
    Optional<ChatRoom> findById(String roomId);
}