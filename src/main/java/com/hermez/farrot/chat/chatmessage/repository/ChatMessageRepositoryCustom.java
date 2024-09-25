package com.hermez.farrot.chat.chatmessage.repository;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.member.entity.Member;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepositoryCustom {

  List<ChatMessage> findAllByChatRoomId(Integer chatRoomId);

  Integer countReadCountByChatRoomId(Integer memberId,Integer chatRoomId);

}
