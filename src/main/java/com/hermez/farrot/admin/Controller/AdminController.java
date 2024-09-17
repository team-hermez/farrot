package com.hermez.farrot.admin.Controller;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

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
        return "admin/admin-member-form";
    }

    @GetMapping("/member")
    public String getContacts(Model model) {
        List<Member> memberList = adminService.getMemberList();
        model.addAttribute("memberList", memberList);
        return "admin/admin-member";
    }

    @ResponseBody
    @PostMapping("/category-sales")
    public String getCategorySalesTop5(Model model) {
       List<AdminCategorySalesTop5Response> categorySalesTop5ResponseList = adminService.getCategorySalesTop5();
       Gson gson = new Gson();
       JsonArray jArray = new JsonArray();

       Iterator<AdminCategorySalesTop5Response> it = categorySalesTop5ResponseList.iterator();

        while(it.hasNext()){
            AdminCategorySalesTop5Response adminCategorySalesTop5Response = it.next();
            JsonObject jsonObject = new JsonObject();

            int count = adminCategorySalesTop5Response.getCount();
            String categoryCode = adminCategorySalesTop5Response.getCategoryCode();

            jsonObject.addProperty("count", count);
            jsonObject.addProperty("categoryCode", categoryCode);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }
}