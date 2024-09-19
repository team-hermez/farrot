package com.hermez.farrot.chat.chatmessage.controller;

import com.hermez.farrot.chat.chatmessage.dto.request.SendMessageRequest;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatResponse;
import com.hermez.farrot.chat.chatmessage.service.ChatMessageService;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

  @MessageMapping("/message/{roomId}")
  @SendTo("/room/{roomId}")
  public ChatResponse sendMessage(@Payload SendMessageRequest request, StompHeaderAccessor accessor) {
    log.info("Sending message: {}", request.senderId());
    Integer readCount = chatMessageService.save(request);
    Integer productId = Integer.valueOf((String) Objects.requireNonNull(accessor.getSessionAttributes()).get("productId"));
    chatMessageService.sendChatNotification(request, productId);
    return ChatResponse.builder()
        .chatRoomId(request.chatRoomId())
        .senderId(request.senderId())
        .nickName(request.nickname())
        .message(request.message())
        .type(request.type())
        .readCount(readCount)
        .sendTime(formatTime(LocalDateTime.now())).build();
  }


  @MessageMapping("/enter/{roomId}")
  public void sendBeforeMessage(@Payload ChatRoomEnterResponse chatRoomEnterResponse, StompHeaderAccessor accessor) {
      chatMessageService.findAllByChatRoomId(chatRoomEnterResponse)
    .forEach(chatMessage -> chatMessageService.sendMessageToUser(accessor.getSessionId(), "/room/" + chatRoomEnterResponse.roomId(), chatMessage));
  }

  private String formatTime(LocalDateTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
    return time.format(formatter);
  }
}
