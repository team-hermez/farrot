package com.hermez.farrot.chat.chatroom.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomEnterResponse(
    Integer roomId, String email,
    Integer senderId, String nickName,
    String displayNickName) {

}
