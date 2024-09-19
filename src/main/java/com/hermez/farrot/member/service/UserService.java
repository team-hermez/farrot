package com.hermez.farrot.member.service;

import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public int save(MemberRegisterRequest memberRegisterRequest) {
        Date now = new Date();
        return memberRepository.save(Member.builder()
                .memberName(memberRegisterRequest.getMemberName())
                .email(memberRegisterRequest.getEmail())
                .password(passwordEncoder.encode(memberRegisterRequest.getPassword()))
                .phone(memberRegisterRequest.getPhone())
                .nickname(memberRegisterRequest.getNickname())
                .createAt(now)
                .role(Role.ROLE_USER)
                .build()).getId();
    }

    @Transactional(readOnly = true)
    @Override
    public void logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response) {

        Optional<Member> optionalMember = memberRepository.findByEmail(memberLoginRequest.getEmail());

        if (optionalMember.isEmpty()){
            log.warn("회원이 존재하지 않음");
        }

        Member member = optionalMember.get();

        if(!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())){
            log.warn("비밀번호가 일치하지 않습니다.");
        }

        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, jwtTokenProvider.generateToken(member.getEmail(),member.getId(),member.getRole()));
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setSecure(false);

        System.out.println(cookie);
        response.addCookie(cookie);
    }
}
