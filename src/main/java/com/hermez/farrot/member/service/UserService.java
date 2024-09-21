package com.hermez.farrot.member.service;

import com.hermez.farrot.image.dto.request.ImageRequest;
import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.image.service.ImageService;
import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.dto.request.MemberUpdateRequest;
import com.hermez.farrot.member.dto.response.MemberImageResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

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

        System.out.println(memberLoginRequest.getPassword());

        if (optionalMember.isEmpty()){
            log.warn("회원이 존재하지 않음");
        }

        Member member = optionalMember.get();

        if(!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())){
            log.warn("비밀번호가 일치하지 않습니다.");
        }
        else {
            Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, jwtTokenProvider.generateToken(member.getEmail(), member.getId(), member.getRole()));
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setPath("/");
            cookie.setDomain(request.getServerName());
            cookie.setSecure(false);

            System.out.println(cookie);
            response.addCookie(cookie);
        }
    }

    @Transactional
    @Override
    public Member userDetail(String jwtToken) {
        Member member =  memberRepository.findByEmail(jwtTokenProvider.parseClaims(jwtToken).getSubject()).get();
        return memberRepository.findByEmail(jwtTokenProvider.parseClaims(jwtToken).getSubject()).orElse(null);
    }

    @Transactional
    @Override
    public MemberImageResponse userImage(Member member) {
        MemberImageResponse memberImageResponse = new MemberImageResponse();
        memberImageResponse.setMemberId(member.getId());

        List<Image> images =  imageService.getImagesByEntity(member);
        if(images.isEmpty()){
            Image defaultImage = new Image("/default-member-image.png");
            images.add(defaultImage);
        }
        memberImageResponse.setImages(images);

        return memberImageResponse;
    }

    @Transactional
    @Override
    public boolean updateUserDetail(MemberUpdateRequest memberUpdateRequest, MultipartFile file) {
        Member member = memberRepository.findByEmail(memberUpdateRequest.getEmail()).orElseThrow(()-> new RuntimeException("회원이 존재하지 않습니다"));
        System.out.println(member.getId().intValue());
        System.out.println(memberUpdateRequest.getExPassword());
        System.out.println(memberUpdateRequest.getNewPassword());

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
        if (!file.isEmpty()) imageService.save(new ImageRequest<>(updateMember, file));

        return true;
    }

    @Override
    public Member getMember(HttpServletRequest request){
        return userDetail(jwtTokenProvider.resolveToken(request));
    }
}
