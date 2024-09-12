package com.hermez.farrot.chat.chatmessage.controller;

import com.google.firebase.database.annotations.Nullable;
import com.hermez.farrot.chat.chatmessage.dto.request.SendMessageRequest;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatRoomResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.service.ChatMessageService;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatMessageController {

  private final ChatMessageService chatMessageService;
  private final MemberRepository memberRepository;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/message/{roomId}")
  @SendTo("/room/{roomId}")
  public ChatResponse sendMessage(
      @Payload SendMessageRequest request){
    log.info("Sending message: {}", request.senderId());
    chatMessageService.save(request.chatRoomId(),request.email() ,request.message(),request.type());
//    simpMessagingTemplate.convertAndSend("/topic/"+request.senderId(), request.message());
    return ChatResponse.builder()
        .chatRoomId(request.chatRoomId())
        .senderId(request.senderId())
        .nickName(request.nickname())
//        .senderType(SenderType.SENDER)
        .message(request.message())
        .type(request.type())
        .readCount(1)
        .sendTime(formatTime(LocalDateTime.now())).build();
  }

  @MessageMapping("/enter/{roomId}")
  public void sendBeforeMessage(
      @ModelAttribute ChatRoomEnterResponse chatRoomEnterResponse,
      StompHeaderAccessor accessor
      ) {
    String sessionId = accessor.getSessionId();
    List<ChatRoomResponse> chatMessages = chatMessageService.findAllByChatRoomId(chatRoomEnterResponse);
    chatMessages.forEach(chatMessage ->{
        log.info("Sending before message: {}", chatMessage);
        chatMessageService.sendMessageToUser(sessionId,"/room/" + chatRoomEnterResponse.roomId(), chatMessage);
        }
    );
  }

  private MessageHeaders createMessageHeaders(@Nullable String sessionId) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
    if(sessionId != null) headerAccessor.setSessionId(sessionId);
    headerAccessor.setLeaveMutable(true);
    return headerAccessor.getMessageHeaders();
  }

  private String formatTime(LocalDateTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
    return time.format(formatter);
  }
}
