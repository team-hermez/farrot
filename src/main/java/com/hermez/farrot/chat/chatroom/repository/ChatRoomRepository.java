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

  public ChatRoom findById(Integer chatRoomId) {
    return em.find(ChatRoom.class, chatRoomId);
  }

  public List<ChatRoom> findAll() {
    return em.createQuery("select c from ChatRoom c "
        + " join fetch c.sender m "
        + " join fetch c.chatMessages cm "
        + " join fetch  c.product p"
        , ChatRoom.class).getResultList();
  }

  public List<ChatRoom> findAllBySenderId(Integer senderId) {
    return em.createQuery("select c from ChatRoom c"
        + " join fetch c.sender m "
        + " join fetch c.product p"
        + " where m.id = :senderId"
            + " order by c.createdAt", ChatRoom.class)
        .setParameter("senderId", senderId)
        .getResultList();
  }

  public List<ChatRoom> findAllByProductId(Integer productId) {
    return em.createQuery("select c from ChatRoom c"
            + " join fetch c.sender m "
            + " join fetch c.product p"
            + " where p.id = :productId"
            + " order by c.createdAt", ChatRoom.class)
        .setParameter("productId", productId)
        .getResultList();
  }

  public Integer findChatRoomIdBySenderId(Integer senderId) {
    return em.createQuery("select cr.id from ChatRoom cr"
            + " where cr.sender.id =:senderId"
            + " order by cr.id desc ", Integer.class)
        .setParameter("senderId", senderId)
        .setMaxResults(1)
        .getSingleResult();

  }
}
