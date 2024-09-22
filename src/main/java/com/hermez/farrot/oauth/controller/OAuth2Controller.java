package com.hermez.farrot.oauth.controller;

import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.service.UserDetailsImpl;
import com.hermez.farrot.oauth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    @GetMapping("/auth/logout")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 쿠키 삭제
        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 소셜 서비스 로그아웃 URL 리다이렉트
        String provider = userDetails.getProvider();
        String logoutUrl = AuthService.getLogoutUrl(provider);
        if (logoutUrl != null) response.sendRedirect(logoutUrl);
        else response.sendRedirect("/");
    }
}
