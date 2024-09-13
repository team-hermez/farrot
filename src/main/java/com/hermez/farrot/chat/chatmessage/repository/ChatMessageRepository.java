package com.hermez.farrot.chat.chatmessage.repository;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

  private final EntityManager em;

  public void save(ChatMessage chatMessage) {
    em.persist(chatMessage);
  }

  public LocalDateTime findLastMessageTime() {
    ChatMessage singleResult = em.createQuery("select max(timestamp) from ChatMessage c",
        ChatMessage.class).getSingleResult();
    return singleResult.getCreatedAt();
  }


public List<ChatMessage> findAllByChatRoomId(Integer chatRoomId){
    return em.createQuery("select c from ChatMessage c where c.chatRoom.id=:chatRoomId order by c.id", ChatMessage.class)
        .setParameter("chatRoomId", chatRoomId)
        .getResultList();
}


  public ChatMessage findById(Integer chatMessageId) {
    return em.find(ChatMessage.class, chatMessageId);
  }

}
