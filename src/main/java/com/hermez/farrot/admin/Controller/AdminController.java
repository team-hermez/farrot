package com.hermez.farrot.admin.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/form")
    public String getAdminMain(Model model) {
        int totalCount = adminService.getMemberTotalCount();
        List<Member> memberList = adminService.getMemberList();
        model.addAttribute("memberList", memberList);
        model.addAttribute("totalCount", totalCount);

        return "admin/admin-form";
    }

    @GetMapping("/member-form")
    public String getMemberForm(Model model) {
        List<Member> memberList = adminService.getMemberList();
        model.addAttribute("memberList", memberList);
        return "admin/admin-member";
    }

    @GetMapping("/contacts")
    public String getContacts(Model model) {
        List<Member> memberList = adminService.getMemberList();
        model.addAttribute("memberList", memberList);
        return "admin/admin-contacts";
    }

    @GetMapping("/chart")
    public String getCharts(){
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();
        return "admin/admin-form";
    }
}