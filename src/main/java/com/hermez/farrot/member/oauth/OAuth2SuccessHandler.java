package com.hermez.farrot.member.oauth;

import com.hermez.farrot.member.entity.Role;
import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.service.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private static final String URI = "/auth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Integer id = userDetails.getId();
        Role role = getRoleFromAuthorities(authentication.getAuthorities());
        String jwtToken = jwtTokenProvider.generateToken(authentication.getName(),id,role);

        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("jwtToken", jwtToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }

    private Role getRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::mapToRole) // 문자열을 Role Enum으로 변환
                .filter(role -> role != null) // null 필터링
                .findFirst() // 첫 번째 역할 반환
                .orElse(Role.ROLE_USER); // 기본값 설정 (USER 역할)
    }

    private Role mapToRole(String authority) {
        try {
            return Role.valueOf(authority); // 권한 문자열을 Role Enum으로 변환
        } catch (IllegalArgumentException e) {
            return null; // 변환 실패 시 null 반환
        }
    }
}
