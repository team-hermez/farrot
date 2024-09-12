package com.hermez.farrot.chat.chatmessage.dto.response;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import lombok.Builder;
@Builder
public record ChatRoomResponse(
    String nickName, Integer senderId,SenderType senderType ,
    String message, ChatMessageType type,
    Integer readCount ,String sendTime) {}
