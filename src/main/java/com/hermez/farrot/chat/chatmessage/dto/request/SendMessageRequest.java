package com.hermez.farrot.chat.chatmessage.dto.request;

import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import lombok.Builder;

@Builder
public record SendMessageRequest(Integer chatRoomId, Integer senderId, SenderType senderType, String nickName,
                                 ChatMessageType type, String message) {}
