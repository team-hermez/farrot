package com.hermez.farrot.oauth.service;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.member.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuthUserService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성하기 위한 의존성

    public void handleOAuthLogin(OAuth2User oAuth2User, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Map<String, Object> attribute = oAuth2User.getAttribute("response");
        String email = attribute.get("email").toString();
        String nickname = attribute.get("nickname").toString(); // 예시로 nickname도 가져오는 경우
        String phone = attribute.get("mobile").toString();
        System.out.println("email: "+email);
        System.out.println("nickname: "+nickname);
        System.out.println("phone: "+phone);
        PrincipalDetails principalDetails = (PrincipalDetails) oAuth2User;
        String provider = principalDetails.attributesKey();
        System.out.println(provider);

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElse(null);

        if (member != null) {
            // 이미 등록된 회원이면 로그인 처리
            response.addCookie(getCookie(member, request));

            String redirectScript = "<script>" +
                    "if (window.opener) {" +
                    "window.opener.location.href = '/';" + // 인덱스 페이지로 이동
                    "window.close();" +
                    "} else {" +
                    "window.location.href = '/';" +
                    "}" +
                    "</script>";

            sendRedirectScript(response, "/");
        } else {
            // 팝업 창에서 부모 창으로 리다이렉트하고 팝업 닫기
            String redirectScript = String.format("<script>" +
                            "if (window.opener) {" +
                            "window.opener.location.href = '/member/register?email=%s&nickname=%s&phone=%s&provider=%s';" +
                            "window.close();" +
                            "} else {" +
                            "window.location.href = '/member/register?email=%s&nickname=%s&phone=%s&provider=%s';" +
                            "}" +
                            "</script>", URLEncoder.encode(email, "UTF-8"),
                    URLEncoder.encode(nickname, "UTF-8"),
                    URLEncoder.encode(phone, "UTF-8"),
                    URLEncoder.encode(provider, "UTF-8"),
                    URLEncoder.encode(email, "UTF-8"),
                    URLEncoder.encode(nickname, "UTF-8"),
                    URLEncoder.encode(phone, "UTF-8"),
                    URLEncoder.encode(provider, "UTF-8"));

            // HTML로 응답을 설정하고 자바스크립트를 반환
            response.setContentType("text/html");
            response.getWriter().write(redirectScript);
        }
    }

    private Cookie getCookie(Member member, HttpServletRequest request) {
        String email = member.getEmail();
        Integer id = member.getId();
        Role role = member.getRole();

        // JWT 토큰 생성
        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, jwtTokenProvider.generateToken(email, id, role));
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 유효
        cookie.setPath("/");
        cookie.setDomain(request.getServerName());
        cookie.setSecure(false); // HTTPS 환경에서 true로 설정
        return cookie;
    }

    private void sendRedirectScript(HttpServletResponse response, String redirectUrl) throws IOException {
        String redirectScript = String.format("<script>window.opener.location.href = '%s'; window.close();</script>", redirectUrl);
        response.setContentType("text/html");
        response.getWriter().write(redirectScript);
    }

}
