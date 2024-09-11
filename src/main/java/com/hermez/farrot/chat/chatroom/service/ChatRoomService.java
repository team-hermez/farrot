package com.hermez.farrot.chat.chatroom.service;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.chat.chatmessage.repository.ChatMessageRepository;
import com.hermez.farrot.chat.chatmessage.repository.query.ChatMessageQueryRepository;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse.ChatRoomsResponseBuilder;
import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  //  private final ChatMessageRepository chatMessageRepository;
  private final ChatMessageQueryRepository chatMessageQueryRepository;

  public List<ChatRoomsResponse> findAll() {
    List<ChatRoom> chatRooms = chatRoomRepository.findAll();
    return chatRooms.stream()
        .map(c -> ChatRoomsResponse.builder()
            .chatRoomId(c.getId())
            .productId(c.getProduct().getId())
            .chatMessageType(chatMessageQueryRepository.findLatestMessageByChatRoomId(c.getId()).chatMessageType())
            .message(chatMessageQueryRepository.findLatestMessageByChatRoomId(c.getId()).latestMessage())
            .readCount(chatMessageQueryRepository.findLatestMessageByChatRoomId(c.getId()).readCount())
            .latestSendTime(chatMessageQueryRepository.findLatestMessageByChatRoomId(c.getId()).latestSendTime())
            .build()).collect(
            Collectors.toList());
  }

}
