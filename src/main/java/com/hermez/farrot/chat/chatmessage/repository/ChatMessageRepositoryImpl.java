package com.hermez.farrot.chat.chatmessage.repository;

import static com.hermez.farrot.chat.chatmessage.entity.QChatMessage.*;
import static com.hermez.farrot.chat.chatroom.entity.QChatRoom.*;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.chat.chatmessage.entity.QChatMessage;
import com.hermez.farrot.chat.chatroom.entity.QChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
//select c from ChatMessage c where c.chatRoom.id=:chatRoomId order by c.id

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
