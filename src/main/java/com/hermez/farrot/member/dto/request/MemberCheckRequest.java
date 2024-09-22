package com.hermez.farrot.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCheckRequest {
    private String email;
    private String nickname;
}
