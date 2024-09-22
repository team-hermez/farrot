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
import com.hermez.farrot.member.exception.NotFoundMemberException;
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
        if(memberRepository.findByEmail(memberRegisterRequest.getEmail()).isPresent()){throw new NotFoundMemberException("이미 존재하는 이메일입니다");}

        LocalDateTime now = LocalDateTime.now();
        return memberRepository.save(Member.builder()
                .memberName(memberRegisterRequest.getMemberName())
                .email(memberRegisterRequest.getEmail())
                .password(passwordEncoder.encode(memberRegisterRequest.getPassword()))
                .phone(memberRegisterRequest.getPhone())
                .nickname(memberRegisterRequest.getNickname())
                .createAt(now)
                .role(Role.ROLE_USER)
                .provider(memberRegisterRequest.getProvider())
                .status(1) // 가입시 활성화
                .build()).getId();
    }

    @Transactional(readOnly = true)
    @Override
    public void logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response, HttpServletRequest request) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberLoginRequest.getEmail());

        Member member = optionalMember.orElseThrow(()-> new NotFoundMemberException("가입되지 않은 이메일입니다"));

        if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new NotFoundMemberException("비밀번호가 틀립니다");
        }else {
            response.addCookie(getCookie(member.getEmail(), member.getId(), member.getRole(), request));
        }
    }


    @Transactional
    @Override
    public Member userDetail(String jwtToken) {
        Member member =  memberRepository.findByEmail(jwtTokenProvider.parseClaims(jwtToken).getSubject()).get();
        return memberRepository.findByEmail(jwtTokenProvider.parseClaims(jwtToken).getSubject()).orElseThrow(()-> new NotFoundMemberException("회원 정보를 찾을 수 없습니다."));
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
        Member member = memberRepository.findByEmail(memberUpdateRequest.getEmail()).orElseThrow(()-> new NotFoundMemberException("회원이 존재하지 않습니다"));
        boolean checkPassword = passwordEncoder.matches(
                memberUpdateRequest.getExPassword(), member.getPassword());
        if (!checkPassword){
            throw new NotFoundMemberException("현재 비밀번호가 일치하지 않습니다.");
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

    private Cookie getCookie(String email, Integer id, Role role, HttpServletRequest request){
        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, jwtTokenProvider.generateToken(email, id, role));
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setDomain(request.getServerName());
        cookie.setSecure(false);

        return cookie;
    }

    public boolean isEmailAvailable(String email){
        return !memberRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameAvailable(String nickname){
        return !memberRepository.findByNickname(nickname).isPresent();
    }

    public boolean validateCurrentPassword(String exPassword, HttpServletRequest request){
        System.out.println("request: "+request);
        String email = jwtTokenProvider.parseClaims(jwtTokenProvider.resolveToken(request)).getSubject();
        System.out.println("test: "+ email);

        return passwordEncoder.matches(exPassword,
                memberRepository.findByEmail(email).get().getPassword());
    }

}
