package com.hermez.farrot.chat.chatmessage.repository;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,ChatMessageRepositoryCustom {

}
