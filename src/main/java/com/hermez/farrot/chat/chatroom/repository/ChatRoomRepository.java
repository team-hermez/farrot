package com.hermez.farrot.chat.chatroom.repository;

import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findAllBySenderId(Integer senderId);

  List<ChatRoom> findAllByProductId(Integer productId);

  @Query("select cr.id from ChatRoom cr where cr.sender.id =:senderId order by cr.id desc")
  List<Integer> findChatRoomIdBySenderId(Integer senderId);

  Optional<ChatRoom> findById(Integer roomId);
}
