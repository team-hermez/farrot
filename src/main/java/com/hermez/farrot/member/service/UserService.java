package com.hermez.farrot.member.service;

import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.dto.request.MemberUpdateRequest;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        LocalDateTime now = LocalDateTime.now();
        return memberRepository.save(Member.builder()
                .memberName(memberRegisterRequest.getMemberName())
                .email(memberRegisterRequest.getEmail())
                .password(passwordEncoder.encode(memberRegisterRequest.getPassword()))
                .phone(memberRegisterRequest.getPhone())
                .nickname(memberRegisterRequest.getNickname())
                .createAt(now)
                .role(Role.ROLE_USER)
                .status(1) // 가입시 활성화
                .build()).getId();
    }

    @Transactional(readOnly = true)
    @Override
    public void logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response, HttpServletRequest request) {

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
        cookie.setDomain(request.getServerName());
        cookie.setSecure(false);

        System.out.println(cookie);
        response.addCookie(cookie);
    }
    @Override
    public Member userDetail(String jwtToken) {
        Optional<Member> member =  memberRepository.findByEmail(jwtTokenProvider.parseClaims(jwtToken).getSubject());
        return memberRepository.findByEmail(jwtTokenProvider.parseClaims(jwtToken).getSubject()).orElse(null);
    }

    @Transactional
    @Override
    public boolean updateUserDetail(MemberUpdateRequest memberUpdateRequest) {
        System.out.println("test: "+memberUpdateRequest.getEmail());
        Member member = memberRepository.findByEmail(memberUpdateRequest.getEmail()).orElseThrow(()-> new RuntimeException("회원이 존재하지 않습니다"));

        boolean checkPassword = passwordEncoder.matches(
                memberUpdateRequest.getExPassword(), member.getPassword());
        if (!checkPassword){
            System.out.println("현재 비밀번호가 다릅니다");
            return false;
        }

        String encodePassword = (memberUpdateRequest.getNewPassword() != null && !memberUpdateRequest.getNewPassword().isEmpty())
                ? passwordEncoder.encode(memberUpdateRequest.getNewPassword())
                : member.getPassword(); // 새로운 비밀번호 없으면 기존 비밀번호 사용

        Member updateMember = member.toBuilder()
                .account(memberUpdateRequest.getAccount())
                .nickname(memberUpdateRequest.getNickname())
                .password(encodePassword)
                .build();
        memberRepository.save(updateMember);
        return true;
    }

    @Override
    public Member getMember(HttpServletRequest request){
        return userDetail(jwtTokenProvider.resolveToken(request));
    }
}
