package com.hermez.farrot.chat.chatroom.repository;

import static com.hermez.farrot.chat.chatroom.entity.QChatRoom.chatRoom;
import static com.hermez.farrot.product.entity.QProduct.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomCustomRepository {

  private final JPAQueryFactory queryFactory;

  public ChatRoomCustomRepository(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public List<Integer> findChatRoomIdBySenderId(Integer senderId) {
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .where(chatRoom.sender.id.eq(senderId))
        .orderBy(chatRoom.id.desc())
        .fetch();
  }

  public List<Integer> findChatRooIdAsSeller(Integer myId, Pageable pageable) {
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .join(chatRoom.product, product)
        .where(product.member.id.eq(myId))
        .fetch();
  }

  public List<Integer> findChatRoomIdAsBuyer(Integer myId, Pageable pageable) {
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .where(chatRoom.sender.id.eq(myId))
        .fetch();
  }
}
