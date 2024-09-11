package com.hermez.farrot.chat.chatroom.repository;

import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

  private final EntityManager em;

  public void save(ChatRoom chatRoom) {
    em.persist(chatRoom);
  }

  public ChatRoom findById(int chatRoomId) {
    return em.find(ChatRoom.class, chatRoomId);
  }

  public List<ChatRoom> findAll() {
    return em.createQuery("select c from ChatRoom c "
//        + " join fetch c.member m "
        + " join fetch c.chatMessages cm "
        + " join fetch  c.product p", ChatRoom.class).getResultList();
  }

}
