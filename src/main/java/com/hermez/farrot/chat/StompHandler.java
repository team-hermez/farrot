package com.hermez.farrot.chat;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.chat.chatmessage.entity.RoomConnect;
import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.service.ChatRoomService;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

  private final ChatRoomService chatRoomService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    log.info("command : {}", accessor.getCommand());
    log.info("session : {}", accessor.getSessionId());
    log.info("session : {}", accessor.getUser());
    log.info("destination : {}", accessor.getDestination());
    log.info("session attribute 1 : {}", accessor.getSessionAttributes());
    log.info("[header] senderId : {}",accessor.getFirstNativeHeader("senderId"));
    log.info("[header] roomId : {}",accessor.getFirstNativeHeader("roomId"));

    String senderId = accessor.getFirstNativeHeader("senderId");
    String roomId = accessor.getFirstNativeHeader("roomId");
    if(StompCommand.CONNECT.equals(accessor.getCommand())) {
      Map<String, Object> attributes = accessor.getSessionAttributes();
        assert attributes != null;
        attributes.put("senderId", senderId);
        attributes.put("roomId", roomId);
        accessor.setSessionAttributes(attributes);

      log.info("session attribute 2 : {}", accessor.getSessionAttributes());
    }
    handleMessage(Objects.requireNonNull(accessor.getCommand()), accessor, senderId, roomId);

    return message;
  }

  @Override
  public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent,
      Exception ex) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if(StompCommand.DISCONNECT.equals(accessor.getCommand())) {
      String roomId = accessor.getFirstNativeHeader("roomId");
      if(roomId != null) {
        log.info("종료 로직 : {}", roomId);
        disConnectToChatRoom(roomId);
      }
    }
  }

  private void handleMessage(StompCommand stompCommand,StompHeaderAccessor accessor, String senderId, String roomId) {
    switch (stompCommand) {
      case CONNECT:
        connectToChatRoom(accessor,roomId,senderId); break;
      case SUBSCRIBE:
      case SEND:
        verifyAccessToken(); break;
    }
  }

  private void disConnectToChatRoom(String roomId) {
    Integer roomId_ = Integer.parseInt(roomId);
    chatRoomService.disconnectChatRoom(roomId_);
  }

  private void connectToChatRoom(StompHeaderAccessor accessor, String roomId,String senderId) {
    Integer roomId_ = Integer.parseInt(roomId);
    Integer senderId_ = Integer.parseInt(senderId);
    chatRoomService.readMessage(roomId_,senderId_);
    chatRoomService.connectChatRoom(roomId_);
  }

  private void verifyAccessToken() {
  }
}
