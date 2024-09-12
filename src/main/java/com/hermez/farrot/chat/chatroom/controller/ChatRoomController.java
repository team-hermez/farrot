package com.hermez.farrot.chat.chatroom.controller;

import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.service.ChatRoomService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/chat-room")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final MemberRepository memberRepository;

  @GetMapping("/enter/{productId}")
  public String enterChatRoom(@PathVariable Integer productId, Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    chatRoomService.createChatRoom(productId);
    Integer roomId = chatRoomService.findBySenderId(sender.getId());
    ChatRoomEnterResponse chatRoomEnterResponse = ChatRoomEnterResponse.builder()
        .roomId(roomId)
        .email(userEmail)
        .senderId(sender.getId())
        .nickName(sender.getNickname())
        .build();
    model.addAttribute("chatRoomEnterResponse", chatRoomEnterResponse);
    return "chat/chat-room";
  }

  @GetMapping("/rooms")
  public String chatRoomsPage(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member findMember = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    List<ChatRoomsResponse> chatRooms = chatRoomService.findAll(findMember.getId());
    model.addAttribute("chatRooms", chatRooms);
    return "chat/chat-rooms";
  }

  @GetMapping("/room")
  public String chatRoomPage(@RequestParam Integer roomId,Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    log.info("나의 아이디 {}",sender.getId());
    ChatRoomEnterResponse chatRoomEnterResponse = ChatRoomEnterResponse.builder()
        .roomId(roomId)
        .email(userEmail)
        .senderId(sender.getId())
        .nickName(sender.getNickname())
        .build();
    model.addAttribute("chatRoomEnterResponse", chatRoomEnterResponse);
    return "chat/chat-room";
  }

}
