package com.hermez.farrot.admin.service.impl;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.repository.AdminRepository;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    public final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Member> getMemberList(){
        return adminRepository.findMemberByOrderById();
    }

    public int getMemberTotalCount(){
        return (int) adminRepository.count();
    }

    @Override
    public List<AdminCategorySalesTop5Response> getCategorySalesTop5() {
        return adminRepository.findCategorySales();
    }

}
