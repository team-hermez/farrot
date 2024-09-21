package com.hermez.farrot.member.service;

import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.dto.request.MemberUpdateRequest;
import com.hermez.farrot.member.dto.response.MemberImageResponse;
import com.hermez.farrot.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    int save(MemberRegisterRequest memberRegisterRequest);
    void logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response, HttpServletRequest request);
    Member userDetail(String jwtToken);
    Member getMember(HttpServletRequest request);
    boolean updateUserDetail(MemberUpdateRequest memberUpdateRequest, MultipartFile file);
    MemberImageResponse userImage(Member member);
}
