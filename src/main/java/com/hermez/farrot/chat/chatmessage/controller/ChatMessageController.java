package com.hermez.farrot.chat.chatmessage.controller;

import com.hermez.farrot.chat.chatmessage.dto.request.SendMessageRequest;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatRoomResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.service.ChatMessageService;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatMessageController {

  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate messagingTemplate;
  private final MemberRepository memberRepository;

  @MessageMapping("/message/{roomId}")
  @SendTo("/room/{roomId}")
  public ChatResponse sendMessage(
      @Payload SendMessageRequest request){
    /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    log.info("세션 유저 정보===={}",sender.getNickname());
    log.info("Sending message: {}", request);*/
    chatMessageService.save(request.chatRoomId(),request.email() ,request.message(),request.type());
    return ChatResponse.builder()
        .chatRoomId(request.chatRoomId())
        .nickName(request.nickname())
        .senderType(SenderType.SENDER)
        .message(request.message())
        .type(request.type())
        .readCount(1)
        .sendTime(formatTime(LocalDateTime.now())).build();
  }

  @MessageMapping("/enter/{roomId}")
  public void sendBeforeMessage(@ModelAttribute ChatRoomEnterResponse chatRoomEnterResponse) {
    log.info("Sending before message: {}", chatRoomEnterResponse.roomId());
    List<ChatRoomResponse> chatMessages = chatMessageService.findAllByChatRoomId(chatRoomEnterResponse);
    chatMessages.forEach(chatMessage ->
        messagingTemplate.convertAndSend("/room/" + chatRoomEnterResponse.roomId(), chatMessage)
    );
  }

  private String formatTime(LocalDateTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
    return time.format(formatter);
  }
}
