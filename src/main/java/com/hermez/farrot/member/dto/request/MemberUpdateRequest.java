package com.hermez.farrot.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {
    private String email;
    private String nickname;
    private String password;
    private String newPassword;
    private String account;

    public MemberUpdateRequest() {}

    @Builder
    public MemberUpdateRequest(String email, String nickname, String password, String account) {
        this.nickname = nickname;
        this.password = password;
        this.account = account;
    }
}
