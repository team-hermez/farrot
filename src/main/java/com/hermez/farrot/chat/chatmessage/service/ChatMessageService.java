package com.hermez.farrot.chat.chatmessage.service;


import com.hermez.farrot.chat.chatmessage.dto.response.ChatRoomResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import com.hermez.farrot.chat.chatmessage.repository.ChatMessageRepository;

import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepository;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.repository.ProductRepository;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;
  private final SimpMessagingTemplate simpMessagingTemplate;

  public void sendMessageToUser(String sessionId,String destination,ChatRoomResponse response){
    MessageHeaders headers = createMessageHeaders(sessionId);
    log.info("Sending message to user {} to destination {}", sessionId, destination);
    simpMessagingTemplate.convertAndSendToUser(sessionId,destination, response,headers);
  }

  private MessageHeaders createMessageHeaders(@Nullable String sessionId) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
    if (sessionId !=null) headerAccessor.setSessionId(sessionId);
    headerAccessor.setLeaveMutable(true);
    return headerAccessor.getMessageHeaders();
  }

  @Transactional
  public void save(int chatRoomId,String userEmail ,String message, ChatMessageType chatMessageType) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId);
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    ChatMessage chatMessage = ChatMessage.createChatMessage(chatRoom, sender, message,chatMessageType);
    chatMessageRepository.save(chatMessage);
  }

  public List<ChatRoomResponse> findAllByChatRoomId(ChatRoomEnterResponse response) {
    List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(response.roomId());
    Integer myId = response.senderId();
    return chatMessages.stream().map(c -> {
      Integer senderId = c.getSender().getId();
      String nickname = c.getSender().getNickname();
          log.info("메시지 보낸 사람 아이디 {}",nickname);
          if(!Objects.equals(senderId, myId)){
            if(c.getReadCount()!=0) c.readMessage();
          }
          return ChatRoomResponse.builder()
              .nickName(nickname)
              .senderId(senderId)
//              .senderType(senderId != myId ? SenderType.RECEIVER : SenderType.SENDER)
              .message(c.getMessage())
              .type(c.getType())
              .readCount(senderId == myId ? c.getReadCount() : null)
              .sendTime(formatTime(c.getCreatedAt()))
              .build();
        }
    ).collect(Collectors.toList());
  }
  private String formatTime(LocalDateTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
    return time.format(formatter);
  }

}
