package com.hermez.farrot.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberLoginRequest {
    private String email;
    private String password;
    private String provider;

    public MemberLoginRequest() {}

    public MemberLoginRequest(String email, String password, String provider) {
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
