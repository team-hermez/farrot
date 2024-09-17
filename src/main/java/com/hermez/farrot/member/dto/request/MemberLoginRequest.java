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

    MemberLoginRequest() {}

    MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
