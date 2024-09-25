package com.hermez.farrot.oauth.service;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.oauth.entity.OAuth2UserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        Member member = saveOrUpdate(oAuth2UserInfo, registrationId);

        return new PrincipalDetails(member, oAuth2UserAttributes, registrationId);
    }

    private Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo, String provider) {
        // 이메일로 사용자 조회, 없으면 새로 생성
        return memberRepository.findByEmail(oAuth2UserInfo.email())
                .map(existingMember -> existingMember.toBuilder()
                        .provider(provider)
                        .attributeCode(oAuth2UserInfo.toString()) // 필요한 속성으로 대체
                        .build())
                .orElseGet(() -> Member.toOAuth2()
                        .memberName(oAuth2UserInfo.name())
                        .email(oAuth2UserInfo.email())
                        .provider(provider)
                        .attributeCode(oAuth2UserInfo.toString()) // 필요한 속성으로 대체
                        .createAt(LocalDateTime.now())
                        .role(Role.ROLE_USER)
                        .status(1)
                        .build());
    }
}
