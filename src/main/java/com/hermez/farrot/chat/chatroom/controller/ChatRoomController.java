package com.hermez.farrot.chat.chatroom.controller;

import com.hermez.farrot.chat.chatroom.dto.SelectOption;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.service.ChatRoomService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/chat-room")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final MemberRepository memberRepository;

  @PostMapping("/enter/{productId}")
  public String enterChatRoom(@PathVariable Integer productId, RedirectAttributes redirectAttributes) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    chatRoomService.createChatRoom(productId);
    Integer roomId = chatRoomService.findBySenderId(sender.getId());
    redirectAttributes.addAttribute("roomId", roomId);
    redirectAttributes.addAttribute("productId", productId);
    return "redirect:/chat-room/room";
  }

  @GetMapping("/rooms")
  public String chatRoomsPage(
      @ModelAttribute("selectOption") SelectOption selectOption,
     @PageableDefault(size = 5) Pageable pageable,
      Model model) {
    List<SelectOption> selectOptions = new ArrayList<>();
    selectOptions.add(new SelectOption("All", "대화 보기"));
    selectOptions.add(new SelectOption("Buy", "구매 대화"));
    selectOptions.add(new SelectOption("Sell", "판매 대화"));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member findMember = memberRepository.findByEmail(userEmail)//쿼리1
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    Page<ChatRoomsResponse> chatRooms = chatRoomService.findAll(findMember.getId(),selectOption.code(),pageable);//쿼리2
    model.addAttribute("selectOptions",selectOptions);
    model.addAttribute("chatRooms", chatRooms);
    return "chat/chat-rooms";
  }


  @GetMapping("/room")
  public String chatRoomPage(@RequestParam Integer roomId,@RequestParam Integer productId,Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("멤버없음"));
    ChatRoomEnterResponse chatRoomEnterResponse = ChatRoomEnterResponse.builder()
        .roomId(roomId)
        .email(userEmail)
        .productId(productId)
        .senderId(sender.getId())
        .nickName(sender.getNickname())
        .build();
    model.addAttribute("chatRoomEnterResponse", chatRoomEnterResponse);
    return "chat/chat-room";
  }

}
