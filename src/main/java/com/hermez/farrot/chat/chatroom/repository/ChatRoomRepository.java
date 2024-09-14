package com.hermez.farrot.chat.chatroom.repository;

import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findById(Integer roomId);

  Page<ChatRoom> findAllBySenderId(Integer senderId,Pageable pageable);

  Page<ChatRoom> findAllByProductId(Integer productId,Pageable pageable);

}
