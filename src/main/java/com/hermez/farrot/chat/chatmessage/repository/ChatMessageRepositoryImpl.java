package com.hermez.farrot.chat.chatmessage.repository;

import static com.hermez.farrot.chat.chatmessage.entity.QChatMessage.chatMessage;
import static com.hermez.farrot.chat.chatroom.entity.QChatRoom.chatRoom;
import static com.hermez.farrot.member.entity.QMember.member;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
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

  @Override
  public Integer countReadCountByChatRoomId(Integer memberId, Integer chatRoomId) {
    Integer i = queryFactory
        .select(chatMessage.readCount.sum())
        .from(chatMessage)
        .where(chatRoomIdEq(chatRoomId),memberIdNe(memberId))
        .fetchOne();
    log.info("Read count is {}", i);
    return i == null ? 0 : i;
  }


  private BooleanExpression chatRoomIdEq(Integer chatRoomId) {
    return chatRoomId != null? chatMessage.chatRoom.id.eq(chatRoomId) : null;
  }

  private BooleanExpression memberIdNe(Integer memberId) {
    return memberId != null? chatMessage.sender.id.ne(memberId) : null;
  }
}
