package com.hermez.farrot.member.oauth;

import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OAuth2Contoller {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 로그인 처리
    @GetMapping("/login/success")
    public Map<String, String> loginSuccess(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            HttpServletResponse response) {
        // Naver로부터 사용자 정보를 가져옴
        String email = principalDetails.getMember().getEmail();
        Integer id = principalDetails.getMember().getId();

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(email, id, principalDetails.getMember().getRole());

        // JWT 토큰을 쿠키에 추가
        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, jwtToken);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // 응답 반환
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "로그인 성공");
        responseMap.put("token", jwtToken);
        return responseMap;
    }

    // 회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRegisterRequest memberRegisterRequest) {
        userService.save(memberRegisterRequest); // 사용자의 정보를 DB에 저장
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // JWT 쿠키 삭제
        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0); // 쿠키 삭제
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 성공");
    }
}
