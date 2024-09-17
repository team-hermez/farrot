package com.hermez.farrot.admin.service;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.member.entity.Member;

import java.util.List;

public interface AdminService {
    List<Member> getMemberList();
    int getMemberTotalCount();

    List<AdminCategorySalesTop5Response> getCategorySalesTop5();
}
