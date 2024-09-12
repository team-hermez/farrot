package com.hermez.farrot.admin.Controller;

import com.hermez.farrot.admin.repository.AdminRepository;
import com.hermez.farrot.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private AdminRepository adminRepository;

    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping("/form")
    public String getAdmin(Model model) {
        int totalCount = (int) adminRepository.count();
        model.addAttribute("totalCount", totalCount);

        return "admin/admin-form";
    }

    @GetMapping("/contacts")
    public String getContacts(Model model) {
        List<Member> memberList = adminRepository.findMemberByOrderById();
        model.addAttribute("memberList", memberList);
        return "admin/admin-contacts";
    }

}