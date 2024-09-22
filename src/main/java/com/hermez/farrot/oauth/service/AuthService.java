package com.hermez.farrot.oauth.service;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String socialLogin(String email, String provider, String attributeCode) {
        // 소셜 로그인 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> registerNewMember(email, provider, attributeCode));

        // JWT 토큰 생성
        return jwtTokenProvider.generateToken(member.getEmail(), member.getId(), member.getRole());
    }

    private Member registerNewMember(String email, String provider, String attributeCode) {
        LocalDateTime now = LocalDateTime.now();
        Member newMember = Member.builder()
                .email(email)
                .provider(provider)
                .attributeCode(attributeCode)
                .createAt(now)
                .role(Role.ROLE_USER)
                .status(1) // 가입시 활성화
                .build();
        return memberRepository.save(newMember);
    }

    public static String getLogoutUrl(String provider) {
        switch (provider) {
            case "NAVER":
                return "https://nid.naver.com/nidlogin.logout";
            case "KAKAO":
                return "https://kauth.kakao.com/oauth/logout";
            default:
                return null;
        }
    }
}

