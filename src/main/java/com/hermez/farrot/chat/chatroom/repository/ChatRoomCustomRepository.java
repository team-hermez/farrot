package com.hermez.farrot.chat.chatroom.repository;

import static com.hermez.farrot.chat.chatroom.entity.QChatRoom.*;

import com.hermez.farrot.chat.chatroom.entity.QChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomCustomRepository {

  private final EntityManager em;
  private JPAQueryFactory queryFactory;

  @PostConstruct
  private void init(){
    queryFactory = new JPAQueryFactory(em);
  }

public List<Integer> findChatRoomIdBySenderId(Integer senderId){
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .where(chatRoom.sender.id.eq(senderId))
        .orderBy(chatRoom.id.desc())
        .fetch();
  }

}
