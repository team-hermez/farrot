package com.hermez.farrot.chat.chatroom.service;

import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepositoryCustom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepository;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final ChatRoomRepositoryCustom chatRoomCustomRepository;
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

  public Page<ChatRoomsResponse> findAll(Integer senderId, String code, Pageable pageable){
    List<Integer> chatRoomIdList = getChatRoomIdList(senderId, code);
    return chatRoomCustomRepository.findAllById(chatRoomIdList,pageable);
  }


  public Integer findBySenderId(Integer senderId) {
    return chatRoomCustomRepository.findChatRoomIdBySenderId(senderId);
  }


  @Transactional
  public void readMessage(Integer roomId, Integer senderId) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new RuntimeException("채팅방이 없습니다."));
    chatRoom.getChatMessages().forEach(chatMessage -> {
      if(!chatMessage.getSender().getId().equals(senderId)) chatMessage.readMessage();
    });
  }

  @Transactional
  public void connectChatRoom(Integer roomId) {
    log.info("커넥트 메서드에 접속");
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new RuntimeException("채팅방이 없습니다."));
    chatRoom.connect();
  }

  @Transactional
  public void disconnectChatRoom(Integer roomId) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("채팅방이 없습니다."));
    chatRoom.disconnect();
  }

  private List<Integer> getChatRoomIdList(Integer senderId, String code) {
    Set<Integer> chatRoomIdSet = new HashSet<>();
    if (Objects.equals(code, "Buy")) {
      chatRoomIdSet.addAll(chatRoomCustomRepository.findChatRoomIdAsBuyer(senderId));
    } else if (Objects.equals(code, "Sell")) {
      chatRoomIdSet.addAll(chatRoomCustomRepository.findChatRoomIdAsSeller(senderId));
    } else {
      chatRoomIdSet.addAll(chatRoomCustomRepository.findChatRoomIdAsBuyer(senderId));
      chatRoomIdSet.addAll(chatRoomCustomRepository.findChatRoomIdAsSeller(
          senderId));
    }
    List<Integer> chatRoomIdList = new ArrayList<>(chatRoomIdSet);
    return chatRoomIdList;
  }
}
