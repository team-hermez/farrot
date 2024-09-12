package com.hermez.farrot.chat;

import java.security.Principal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {


  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    log.info("command : {}", accessor.getCommand());
    log.info("session : {}", accessor.getSessionId());
    log.info("session : {}", accessor.getUser());
    log.info("destination : {}", accessor.getDestination());
    log.info("session attribute 1 : {}", accessor.getSessionAttributes());
    log.info("senderId header : {}",accessor.getFirstNativeHeader("senderId"));
    log.info("sessionUsername header : {}",accessor.getFirstNativeHeader("sessionUsername"));

    if(StompCommand.CONNECT.equals(accessor.getCommand())) {
      Map<String, Object> attributes = accessor.getSessionAttributes();
      String senderId = accessor.getFirstNativeHeader("senderId");
      String sessionUsername = accessor.getFirstNativeHeader("sessionUsername");
        assert attributes != null;
        attributes.put("senderId", senderId);
        attributes.put("sessionUsername", sessionUsername);
        accessor.setSessionAttributes(attributes);

      log.info("session attribute 2 : {}", accessor.getSessionAttributes());

    }

    return message;
  }
}
