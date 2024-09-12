package com.hermez.farrot.chat.chatmessage.repository.query;

import com.hermez.farrot.chat.chatmessage.dto.response.LatestMessageResponse;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageQueryRepository {

  private final EntityManager em;

  public LatestMessageResponse findLatestMessageByChatRoomId(Integer chatRoomId) {
    List<LatestMessageResponse> result = em.createQuery(
            "select new com.hermez.farrot.chat.chatmessage.dto.response.LatestMessageResponse(c.type,c.message,c.readCount,c.createdAt)"
                + " from ChatMessage c "
                + " where c.chatRoom.id=:chatRoomId order by c.createdAt desc",
            LatestMessageResponse.class)
        .setParameter("chatRoomId", chatRoomId)
        .setMaxResults(1)
        .getResultList();
    return result.isEmpty() ? LatestMessageResponse.builder().chatMessageType(ChatMessageType.TEXT).build() : result.get(0);
  }

}
