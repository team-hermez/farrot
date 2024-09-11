package com.hermez.farrot.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterRequest {
    private String memberName;
    private String nickname;
    private String email;
    private String password;
    private String phone;


    public MemberRegisterRequest(){};

    public MemberRegisterRequest(String memberName, String nickname, String email, String password, String phone) {
        this.memberName = memberName;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;

    }

}
