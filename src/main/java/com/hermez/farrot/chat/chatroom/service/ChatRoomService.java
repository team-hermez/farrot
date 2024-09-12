package com.hermez.farrot.chat.chatroom.service;

import com.hermez.farrot.chat.chatmessage.entity.ChatMessage;
import com.hermez.farrot.chat.chatmessage.repository.ChatMessageRepository;
import com.hermez.farrot.chat.chatmessage.repository.query.ChatMessageQueryRepository;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse.ChatRoomsResponseBuilder;
import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepository;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  //  private final ChatMessageRepository chatMessageRepository;
  private final ChatMessageQueryRepository chatMessageQueryRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void createChatRoom(Integer productId) {
    Product product = productRepository.findById(productId)
            .orElseThrow(()->new RuntimeException("Product not found"));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    ChatRoom chatRoom = ChatRoom.makeChatRoom(sender, product);
    chatRoomRepository.save(chatRoom);
  }

  public List<ChatRoomsResponse> findAll(Integer senderId) {
    List<ChatRoom> chatRooms1 = chatRoomRepository.findAllBySenderId(senderId);
    List<ChatRoom> chatRooms2 = new ArrayList<>();
    productRepository.findAllByMemberId(senderId).forEach(
        product -> chatRooms2.addAll(chatRoomRepository.findAllByProductId(product.getId()))
    );
    List<ChatRoom> allChatRooms = new ArrayList<>(chatRooms1);
    allChatRooms.addAll(chatRooms2);
    allChatRooms.forEach(
        chatRoom -> log.info("챗룸: {}",chatRoom.getId())
    );
   return allChatRooms.stream()
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

  public Integer findBySenderId(Integer senderId) {
    return chatRoomRepository.findChatRoomIdBySenderId(senderId);
  }
}
