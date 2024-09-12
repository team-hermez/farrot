package com.hermez.farrot.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {


  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext != null) {
      attributes.put("SECURITY_CONTEXT", securityContext);
    }
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {

  }
}
