package com.hermez.farrot.chat.chatroom.controller;

import com.hermez.farrot.chat.chatmessage.service.ChatMessageService;
import com.hermez.farrot.chat.chatroom.dto.SelectOption;
import com.hermez.farrot.chat.chatroom.dto.request.ChatRoomRequest;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomEnterResponse;
import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.dto.response.ReadCountResponse;
import com.hermez.farrot.chat.chatroom.service.ChatRoomService;
import com.hermez.farrot.image.service.FirebaseService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/chat-room")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final MemberRepository memberRepository;
  private final ChatMessageService chatMessageService;
  private final FirebaseService firebaseService;

  @PostMapping("/enter/{productId}")
  public String enterChatRoom(@PathVariable Integer productId, RedirectAttributes redirectAttributes) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다."));
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
      @AuthenticationPrincipal UserDetails userDetails,
      Model model) {
    if (userDetails == null) return "redirect:/member/login";
    String userEmail = userDetails.getUsername();
    List<SelectOption> selectOptions = getSelectChatRoomOptions();
    Member findMember = memberRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("멤버없음"));
    Page<ChatRoomsResponse> chatRooms = chatRoomService.findAll(findMember.getId(),selectOption.code(),pageable);
    model.addAttribute("selectOptions",selectOptions);
    model.addAttribute("chatRooms", chatRooms);
    return "chat/chat-rooms";
  }

  @ResponseBody
  @GetMapping("/notification")
  public ReadCountResponse getReadCount(@AuthenticationPrincipal UserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    Member findMember = memberRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("멤버없음"));
    List<ChatRoomsResponse> chatRooms = chatRoomService.findBasicAll(findMember.getId(),"All");
    int sum = chatRooms
        .stream()
        .map(c -> chatMessageService.getReadCount(findMember.getId(), c.chatRoomId()))
        .toList()
        .stream().mapToInt(Integer::intValue)
        .sum();
    return new ReadCountResponse(sum);
  }

  @GetMapping("/room")
  public String chatRoomPage(@RequestParam Integer roomId,@RequestParam Integer productId,@AuthenticationPrincipal UserDetails userDetails,Model model) {
    if (userDetails == null) return "redirect:/member/login";
    String userEmail = userDetails.getUsername();
    String publicImageUrl = firebaseService.getPublicImageUrl("farrotlogo.png");
    Member sender = memberRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("멤버없음"));
    ChatRoomEnterResponse chatRoomEnterResponse = ChatRoomEnterResponse.builder()
        .roomId(roomId)
        .email(userEmail)
        .productId(productId)
        .senderId(sender.getId())
        .nickName(sender.getNickname())
        .build();
    model.addAttribute("chatRoomEnterResponse", chatRoomEnterResponse);
    model.addAttribute("publicImageUrl", publicImageUrl);
    return "chat/chat-room";
  }

  @ResponseBody
  @PostMapping("/room")
  public ChatRoomEnterResponse chatRoomPageReload(@RequestBody ChatRoomRequest chatRoomRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    Member sender = memberRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("멤버없음"));
    return ChatRoomEnterResponse.builder()
        .roomId(chatRoomRequest.roomId())
        .email(userEmail)
        .productId(chatRoomRequest.productId())
        .senderId(sender.getId())
        .nickName(sender.getNickname())
        .build();
  }

  @ResponseBody
  @PostMapping("/get/read-count")
  public ReadCountResponse getReadCount(@AuthenticationPrincipal UserDetails userDetails,@RequestBody String roomId) {
    String userEmail = userDetails.getUsername();
    Member member = memberRepository.findByEmail(userEmail).orElseThrow();
    Integer readCount = chatMessageService.getReadCount(member.getId(), Integer.parseInt(roomId.substring(7).trim()));
    return new ReadCountResponse(readCount);
  }



  private static List<SelectOption> getSelectChatRoomOptions() {
    List<SelectOption> selectOptions = new ArrayList<>();
    selectOptions.add(new SelectOption("All", "대화 보기"));
    selectOptions.add(new SelectOption("Buy", "구매 대화"));
    selectOptions.add(new SelectOption("Sell", "판매 대화"));
    return selectOptions;
  }

}
