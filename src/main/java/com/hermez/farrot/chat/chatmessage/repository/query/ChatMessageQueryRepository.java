package com.hermez.farrot.chat.chatmessage.repository.query;

import static com.hermez.farrot.chat.chatmessage.entity.QChatMessage.chatMessage;

import com.hermez.farrot.chat.chatmessage.dto.response.LatestMessageResponse;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ChatMessageQueryRepository {

  private final JPAQueryFactory queryFactory;

  public ChatMessageQueryRepository(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public LatestMessageResponse findLatestMessageByChatRoomId(Integer chatRoomId) {
    List<LatestMessageResponse> result = queryFactory
        .select(Projections.constructor(LatestMessageResponse.class,
            chatMessage.type, chatMessage.message,
            chatMessage.readCount, chatMessage.createdAt
        ))
        .from(chatMessage)
        .where(chatMessage.chatRoom.id.eq(chatRoomId))
        .orderBy(chatMessage.createdAt.desc())
        .fetch();
    return result.isEmpty() ? LatestMessageResponse.builder().chatMessageType(ChatMessageType.TEXT)
        .build() : result.get(0);
  }

}
