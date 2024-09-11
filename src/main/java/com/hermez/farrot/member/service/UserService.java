package com.hermez.farrot.member.service;

import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Integer save(MemberRegisterRequest memberRegisterRequest) {
        return memberRepository.save(Member.builder()
                .memberName(memberRegisterRequest.getMemberName())
                .email(memberRegisterRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(memberRegisterRequest.getPassword()))
                .phone(memberRegisterRequest.getPhone())
                .nickname(memberRegisterRequest.getNickname())
                .build()).getId();
    }
}
