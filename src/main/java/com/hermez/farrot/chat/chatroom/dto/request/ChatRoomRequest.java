package com.hermez.farrot.chat.chatroom.dto.request;

import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomRequest(String nickName , String message, ChatMessageType type,
                              LocalDateTime sendTime) {

}
