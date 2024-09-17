package com.hermez.farrot.chat.chatmessage.dto.response;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatResponse(
    Integer chatRoomId,String nickName,
    Integer senderId, String message,
    ChatMessageType type, Integer readCount,
    String sendTime) {}
