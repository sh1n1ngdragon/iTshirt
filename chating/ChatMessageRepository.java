package com.shop.chating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
   List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(Long chatRoomId);
}
