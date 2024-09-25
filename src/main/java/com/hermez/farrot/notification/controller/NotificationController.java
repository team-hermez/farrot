package com.hermez.farrot.notification.controller;


import com.hermez.farrot.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

  private final NotificationService notificationService;


  @PostMapping("/notification/register")
  public ResponseEntity<String> register(@RequestBody String token){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    notificationService.register(userEmail, token);
    return ResponseEntity.ok().build();
  }


}
