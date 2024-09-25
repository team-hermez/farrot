package com.hermez.farrot.member.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public JwtToken(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
