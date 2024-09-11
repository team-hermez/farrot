package com.hermez.farrot.chat.chatroom.dto.response;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomsResponse(
    int chatRoomId, int productId,
    ChatMessageType chatMessageType,
    String message, LocalDateTime latestSendTime,
Integer readCount) {}
