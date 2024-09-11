package com.hermez.farrot.chat.chatroom.controller;

import com.hermez.farrot.chat.chatroom.dto.response.ChatRoomsResponse;
import com.hermez.farrot.chat.chatroom.service.ChatRoomService;
import com.hermez.farrot.product.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.SendTo;
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

  @GetMapping("/rooms")
  public String chatRoomsPage(Model model) {
    List<ChatRoomsResponse> chatRooms = chatRoomService.findAll();
    model.addAttribute("chatRooms", chatRooms);
    return "chat/chat-rooms";
  }

  @GetMapping("/room")
  public String chatRoomPage(@RequestParam Integer roomId,Model model) {
    int myId = 2;
    model.addAttribute("memberId", myId);
    model.addAttribute("roomId", roomId);
    return "chat/chat-room";
  }

}
