package com.hermez.farrot.member.service;

import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    int save(MemberRegisterRequest memberRegisterRequest);
    void logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response);
}
