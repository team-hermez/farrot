package com.hermez.farrot.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String reloadScript = "<script>" +
                "alert('소셜 로그인에 실패했습니다. 다시 시도해주세요.');" +  // 오류 메시지 표시
                "window.location.href = '/oauth2/authorization/naver';" + // 소셜 로그인 페이지로 리로딩
                "</script>";

        response.setContentType("text/html");
        response.getWriter().write(reloadScript);
    }
}
