package com.hermez.farrot.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    private String email;
    private String nickname;
    private String exPassword;
    private String newPassword;
    private String account;

    public MemberUpdateRequest() {}

    public MemberUpdateRequest(String email, String nickname, String exPassword,String newPassword, String account) {
        this.email = email;
        this.nickname = nickname;
        this.exPassword = exPassword;
        this.newPassword = newPassword;
        this.account = account;
    }
}
