package com.hermez.farrot.admin.service;

import com.hermez.farrot.member.entity.Member;

import java.util.List;

public interface AdminService {
    List<Member> getMemberList();
    int getMemberTotalCount();
}
