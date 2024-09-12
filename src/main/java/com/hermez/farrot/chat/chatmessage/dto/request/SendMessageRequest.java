package com.hermez.farrot.chat.chatmessage.dto.request;

import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import lombok.Builder;

@Builder
public record SendMessageRequest(Integer chatRoomId, String email,Integer senderId, SenderType senderType, String nickname,
                                 ChatMessageType type, String message) {}
