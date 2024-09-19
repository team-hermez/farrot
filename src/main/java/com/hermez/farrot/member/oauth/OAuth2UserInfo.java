package com.hermez.farrot.member.oauth;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import jakarta.security.auth.message.AuthException;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public record OAuth2UserInfo(
    String name,
    String email,
    String profile
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) throws AuthException {
        return switch (registrationId) {
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new AuthException("illegal registration id");
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();

    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();

    }

    public Member toEntity() {
        return Member.builder()
                .memberName(name)
                .email(email)
                .build();
    }
}
