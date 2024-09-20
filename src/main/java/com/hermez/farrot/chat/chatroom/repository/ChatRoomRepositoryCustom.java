package com.hermez.farrot.chat.chatroom.repository;

import static com.hermez.farrot.chat.chatmessage.entity.QChatMessage.chatMessage;
import static com.hermez.farrot.chat.chatroom.entity.QChatRoom.chatRoom;
import static com.hermez.farrot.product.entity.QProduct.product;
import static com.querydsl.core.types.Projections.constructor;

import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ChatRoomRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ChatRoomRepositoryCustom(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public Page<ChatRoomsResponse> findAllById(List<Integer> chatRoomIds, Pageable pageable) {
    List<ChatRoomsResponse> allContent = chatRoomIds.stream().map(
            chatRoomId ->
                queryFactory
                    .select(constructor(ChatRoomsResponse.class,
                        chatRoom.id, chatRoom.product.id,
                        chatRoom.product.productName, chatMessage.type,
                        chatMessage.message, chatMessage.createdAt,
                        chatMessage.readCount))
                    .from(chatMessage)
                    .join(chatMessage.chatRoom, chatRoom)
                    .where(chatRoom.id.eq(chatRoomId))
                    .orderBy(chatMessage.createdAt.desc())
                    .fetchFirst()
        )
        .filter(Objects::nonNull)
        .toList();

    List<ChatRoomsResponse> content = getContent(pageable, allContent);

    return new PageImpl<>(content, pageable, allContent.size());
  }

  public List<ChatRoomsResponse> findAllBasicById(List<Integer> chatRoomIds) {
   return chatRoomIds.stream().map(
            chatRoomId ->
                queryFactory
                    .select(constructor(ChatRoomsResponse.class,
                        chatRoom.id, chatRoom.product.id,
                        chatRoom.product.productName, chatMessage.type,
                        chatMessage.message, chatMessage.createdAt,
                        chatMessage.readCount))
                    .from(chatMessage)
                    .join(chatMessage.chatRoom, chatRoom)
                    .where(chatRoom.id.eq(chatRoomId))
                    .orderBy(chatMessage.createdAt.desc())
                    .fetchFirst()
        )
        .filter(Objects::nonNull)
        .toList();
  }


  public Integer findChatRoomIdBySenderId(Integer senderId) {
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .where(chatRoom.sender.id.eq(senderId))
        .orderBy(chatRoom.id.desc())
        .fetchFirst();
  }

  public List<Integer> findChatRoomIdAsSeller(Integer myId) {
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .join(chatRoom.product, product)
        .where(product.member.id.eq(myId))
        .fetch();
  }

  public List<Integer> findChatRoomIdAsBuyer(Integer myId) {
    return queryFactory
        .select(chatRoom.id)
        .from(chatRoom)
        .where(chatRoom.sender.id.eq(myId))
        .fetch();
  }

  private static List<ChatRoomsResponse> getContent(Pageable pageable,
      List<ChatRoomsResponse> allContent) {
    int pageSize = pageable.getPageSize();
    int pageNumber = pageable.getPageNumber();

    int start = Math.min(pageNumber * pageSize, allContent.size());
    int end = Math.min(start + pageSize, allContent.size());
    return allContent.subList(start, end);
  }

  public Member findBuyerByChatRoomId(Integer chatRoomId){
    return queryFactory
        .select(chatRoom.sender)
        .from(chatRoom)
        .where(chatRoom.id.eq(chatRoomId))
        .fetchOne();
  }

  //나중에 지울거
  public Member findSellerByProductId(Integer productId){
    return queryFactory
        .select(product.member)
        .from(product)
        .where(product.id.eq(productId))
        .fetchOne();
  }


}
