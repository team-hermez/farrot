package com.hermez.farrot.chat.chatroom.service;

import com.hermez.farrot.chat.chatmessage.dto.response.LatestMessageResponse;
import com.hermez.farrot.chat.chatmessage.repository.ChatMessageRepository;
import com.hermez.farrot.chat.chatmessage.repository.query.ChatMessageQueryRepository;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomCustomRepository;
import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepository;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  private final ChatRoomCustomRepository chatRoomCustomRepository;
  private final ChatMessageRepository chatMessageRepository;
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

  /*public List<ChatRoomsResponse> findChatRoomAsSeller(Integer myId, Pageable pageable){
    List<LatestMessageResponse> latestMessageResponse = new ArrayList<>();
     chatRoomCustomRepository.findChatRooIdAsSeller(myId, pageable)
            .forEach(id -> latestMessageResponse.add(chatMessageQueryRepository.findLatestMessageByChatRoomId(id))
            );
    return latestMessageResponse.stream().map(m->new ChatRoomsResponse(m.)).collect(Collectors.toList())
  }

  public List<ChatRoomsResponse> findChatRoomAsBuyer(Integer myId){

  }*/



//  public List<ChatRoomsResponse> findAll(Integer senderId, Pageable pageable) {
//    Page<ChatRoom> chatRooms1 = chatRoomRepository.findAllBySenderId(senderId,pageable);
//    Page<ChatRoom> chatRooms2 = new PageImpl<>(chatRooms1.getContent());
//    productRepository.findByMemberId(senderId).forEach(p->
//    chatRooms2.addAll(chatRoomRepository.findAllByProductId(p.getId(),pageable)));
//    List<ChatRoom> allChatRooms = new ArrayList<>(chatRooms1);
//    allChatRooms.addAll(chatRooms2);
//    List<ChatRoomsResponse> chatRoomsResponses = new ArrayList<>();
//    allChatRooms.forEach(
//        chatRoom -> {
//          LatestMessageResponse response = chatMessageQueryRepository.findLatestMessageByChatRoomId(chatRoom.getId());
//          ChatRoomsResponse roomsResponse = ChatRoomsResponse.builder()
//              .chatRoomId(chatRoom.getId())
//              .productId(chatRoom.getProduct().getId())
//              .productName(chatRoom.getProduct().getProductName())
//              .chatMessageType(response.chatMessageType())
//              .message(response.latestMessage())
//              .readCount(response.readCount())
//              .latestSendTime(response.latestSendTime())
//              .build();
//          chatRoomsResponses.add(roomsResponse);
//        }
//    );
//    return chatRoomsResponses;
//  }
public Page<ChatRoomsResponse> findAll(Integer senderId, Pageable pageable) {
  // SenderId에 따른 채팅방 조회 (페이징 처리)
  Page<ChatRoom> chatRoomsPage1 = chatRoomRepository.findAllBySenderId(senderId, pageable);

  // ProductId에 따른 채팅방 조회 (페이징 처리)
  List<ChatRoom> chatRoomsFromProducts = productRepository.findByMemberId(senderId).stream()
      .flatMap(p -> chatRoomRepository.findAllByProductId(p.getId(), pageable).getContent().stream())
      .toList();

  // 모든 채팅방 목록을 페이지로 변환
  List<ChatRoom> allChatRooms = new ArrayList<>(chatRoomsPage1.getContent());
  allChatRooms.addAll(chatRoomsFromProducts);

  // ChatRoomsResponse로 변환
  List<ChatRoomsResponse> chatRoomsResponses = allChatRooms.stream()
      .map(chatRoom -> {
        LatestMessageResponse response = chatMessageQueryRepository.findLatestMessageByChatRoomId(chatRoom.getId());
        return ChatRoomsResponse.builder()
            .chatRoomId(chatRoom.getId())
            .productId(chatRoom.getProduct().getId())
            .productName(chatRoom.getProduct().getProductName())
            .chatMessageType(response.chatMessageType())
            .message(response.latestMessage())
            .readCount(response.readCount())
            .latestSendTime(response.latestSendTime())
            .build();
      })
      .collect(Collectors.toList());

  // 페이징을 위해 총 페이지 수를 설정합니다 (예: 전체 데이터 수의 카운트)
  int total = chatRoomsResponses.size(); // 실제 데이터 수에 따라 계산 필요

  // 결과를 Page로 변환하여 반환
  return new PageImpl<>(chatRoomsResponses, pageable, total);
}
  public Integer findBySenderId(Integer senderId) {
    return chatRoomCustomRepository.findChatRoomIdBySenderId(senderId).get(0);
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
}
