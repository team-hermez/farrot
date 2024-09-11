package com.hermez.farrot.chat.chatmessage.service;


import com.hermez.farrot.chat.chatmessage.dto.response.ChatRoomResponse;
import com.hermez.farrot.chat.chatmessage.dto.response.SenderType;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.chat.chatmessage.entity.ChatMessageType;
import com.hermez.farrot.chat.chatmessage.repository.ChatMessageRepository;

import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepository;
import com.hermez.farrot.member.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
//  private final MemberRepository memberRepository;

  @Transactional
  public void save(int chatRoomId, int senderId,String nickName, String message, ChatMessageType chatMessageType) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId);
//    Member sender = memberRepository.findByNickname(nickName);
    Member sender = new Member();
    ChatMessage chatMessage = ChatMessage.createChatMessage(chatRoom, sender, message,chatMessageType);
    chatMessageRepository.save(chatMessage);
  }
  /*String nickName, SenderType senderType , String message, ChatMessageType type,
  LocalDateTime sendTime*/

  @Transactional
  public List<ChatRoomResponse> findAllByChatRoomId(Integer chatRoomId) {
    List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);
    int myId = 2;
    return chatMessages.stream().map(c -> {
        if(c.getSender().getId()!=myId){
          if(c.getReadCount()!=0) c.readMessage();
        }
          return ChatRoomResponse.builder()
              .nickName("홍")
              .senderType(
                  c.getSender().getId() != myId ? SenderType.RECEIVER : SenderType.SENDER
              )
              .message(c.getMessage())
              .type(c.getType())
              .readCount(
                  c.getSender().getId() == myId ? c.getReadCount() : null
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
