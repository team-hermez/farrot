package com.hermez.farrot.chat.chatmessage.repository;

import static com.hermez.farrot.chat.chatmessage.entity.QChatMessage.chatMessage;
import static com.hermez.farrot.chat.chatroom.entity.QChatRoom.chatRoom;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ChatMessageRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<ChatMessage> findAllByChatRoomId(Integer chatRoomId) {
    return queryFactory
        .selectFrom(chatMessage)
        .where(chatRoom.id.eq(chatRoomId))
        .orderBy(chatRoom.id.asc())
        .fetch();
  }
}
