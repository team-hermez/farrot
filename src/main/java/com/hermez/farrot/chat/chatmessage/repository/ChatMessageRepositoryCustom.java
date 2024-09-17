package com.hermez.farrot.chat.chatmessage.repository;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepositoryCustom {

  List<ChatMessage> findAllByChatRoomId(Integer chatRoomId);

}
