package com.hermez.farrot.member.oauth;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @SneakyThrows
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        Member member = saveOrUpdate(oAuth2UserInfo);

        return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
    }
    private Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo) {
        Member member = memberRepository.findByEmail(oAuth2UserInfo.email())
                .orElseGet(oAuth2UserInfo::toEntity);

        return memberRepository.save(member);
    }
}
