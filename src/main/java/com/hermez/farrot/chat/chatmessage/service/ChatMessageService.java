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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  @Transactional
  public void save(int chatRoomId,String userEmail ,String message, ChatMessageType chatMessageType) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId);
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    ChatMessage chatMessage = ChatMessage.createChatMessage(chatRoom, sender, message,chatMessageType);
    chatMessageRepository.save(chatMessage);
  }
  /*String nickName, SenderType senderType , String message, ChatMessageType type,
  LocalDateTime sendTime*/

  @Transactional
  public List<ChatRoomResponse> findAllByChatRoomId(ChatRoomEnterResponse response) {
    List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(response.roomId());
    int myId = response.senderId();
    return chatMessages.stream().map(c -> {
      Integer senderId = c.getSender().getId();
      if(senderId !=myId){
          if(c.getReadCount()!=0) c.readMessage();
        }
          return ChatRoomResponse.builder()
              .nickName(
                  senderId != myId ?
                      productRepository.findById(c.getChatRoom().getProduct().getId())
                          .orElseThrow(()->new RuntimeException("해당하는 아이템이 없습니다."))
                          .getMember().getNickname()
                      : response.nickName()
              )
              .senderType(
                  senderId != myId ? SenderType.RECEIVER : SenderType.SENDER
              )
              .message(c.getMessage())
              .type(c.getType())
              .readCount(
                  senderId == myId ? c.getReadCount() : null
              )
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
