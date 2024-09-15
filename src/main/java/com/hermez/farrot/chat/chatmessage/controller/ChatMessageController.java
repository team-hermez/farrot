package com.hermez.farrot.chat.chatmessage.controller;

import com.hermez.farrot.chat.chatmessage.dto.request.SendMessageRequest;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.ChatRoomResponse;
import com.hermez.farrot.chat.chatmessage.exception.NoMatchUniqueReceiverException;
import com.hermez.farrot.chat.chatmessage.service.ChatMessageService;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepositoryCustom;
import com.hermez.farrot.chat.chatroom.service.ChatRoomService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.member.service.UserDetailsServiceImpl;
import com.hermez.farrot.notification.service.NotificationService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatMessageController {

  private final ChatMessageService chatMessageService;
  private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;
  private final MemberRepository memberRepository;
  private final ChatRoomService chatRoomService;
  private final NotificationService notificationService;
  private final UserDetailsServiceImpl userDetailsService;
  private static Map<String, String> chatStore = new HashMap<>();

  @MessageMapping("/message/{roomId}")
  @SendTo("/room/{roomId}")
  public ChatResponse sendMessage(
      @Payload SendMessageRequest request,
      StompHeaderAccessor accessor) {
    log.info("Sending message: {}", request.senderId());
    chatMessageService.save(request.chatRoomId(), request.email(), request.message(),
        request.type());
    Integer productId = Integer.valueOf(
        (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("productId"));
    Member seller = chatRoomRepositoryCustom.findSellerByProductId(productId);
    Member buyer = chatRoomRepositoryCustom.findBuyerByChatRoomId(request.chatRoomId());
    Member sender = memberRepository.findById(request.senderId())
        .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
    if (Objects.equals(sender.getId(), seller.getId())) {
      log.info("판매자에게 보내기");
      notificationService.creatNotification(sender, buyer);
    } else if (Objects.equals(sender.getId(), buyer.getId())) {
      log.info("구매자에게 보내기");
      notificationService.creatNotification(sender, seller);
    } else {
      throw new NoMatchUniqueReceiverException("알림을 받을 유저를 찾을 수 없습니다.");
    }
    return ChatResponse.builder()
        .chatRoomId(request.chatRoomId())
        .senderId(request.senderId())
        .nickName(request.nickname())
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
    List<ChatRoomResponse> chatMessages = chatMessageService.findAllByChatRoomId(
        chatRoomEnterResponse);
    chatMessages.forEach(chatMessage -> {
          log.info("Sending before message: {}", chatMessage);
          chatMessageService.sendMessageToUser(sessionId, "/room/" + chatRoomEnterResponse.roomId(),
              chatMessage);
        }
    );
  }

  private String formatTime(LocalDateTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
    return time.format(formatter);
  }
}
