package com.hermez.farrot.chat.chatmessage.dto.response;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record LatestMessageResponse(
    ChatMessageType chatMessageType, String latestMessage,
    Integer readCount, LocalDateTime latestSendTime) {

}
