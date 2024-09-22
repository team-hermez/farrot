package com.hermez.farrot.oauth.entity;

import java.util.Map;

public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new RuntimeException("Unsupported registration ID");
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        return new OAuth2UserInfo(
                (String) profile.get("nickname"),
                (String) account.get("email"),
                (String) profile.get("profile_image_url")
        );
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuth2UserInfo(
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("profile_image")
        );
    }
}
