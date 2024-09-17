package com.hermez.farrot.admin.Controller;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String getMainForm(Model model) {
        int totalCount = adminService.getMemberTotalCount();
        model.addAttribute("totalCount", totalCount);

        return "admin/form/admin-form";
    }

    @GetMapping("/member-form")
    public String getMemberForm(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Member> memberList = adminService.getMemberList(pageable);
        model.addAttribute("memberList", memberList);
        return "admin/form/admin-member-form";
    }

    @GetMapping("/member")
    public String getMembers(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Member> memberList = adminService.getMemberList(pageable);
        model.addAttribute("memberList", memberList);
        return "admin/member/admin-member";
    }

    @GetMapping("/report")
    public String getReports(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Member> memberList = adminService.getMemberList(pageable);
        model.addAttribute("memberList", memberList);
        return "admin/member/admin-report";
    }

    @GetMapping("/product-manage")
    public String getProducts(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Product> productList = adminService.getProductList(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product";
    }

    @GetMapping("/products/today")
    public String getProductsToday(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Product> productList = adminService.findProductsSoldToday(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product-today";
    }

    @GetMapping("/products/weekly")
    public String getProductsWeekly(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Product> productList = adminService.getProductList(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product-weekly";
    }

    @GetMapping("/products/monthly")
    public String getProductsMonthly(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Product> productList = adminService.getProductList(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product-monthly";
    }

    @GetMapping("/chat-manage")
    public String getChat(Model model) {
        return "admin/chat/admin-chat";
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