package com.hermez.farrot.admin.Controller;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.dto.AdminProductMonthTotalSalesResponse;
import com.hermez.farrot.admin.dto.AdminRegisterWeeklyResponse;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        int totalResisterCount = adminService.countByCreatedAtToday();

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalResisterCount", totalResisterCount);

        return "admin/form/admin-form";
    }

    @GetMapping("/member")
    public String getMembers(Model model, @PageableDefault(size = 6) Pageable pageable) {
        Page<Member> memberList = adminService.getMemberList(pageable);
        model.addAttribute("memberList", memberList);
        return "admin/member/admin-member";
    }

    @GetMapping("/member-detail/{id}")
    public String getMemberDetail(@PageableDefault(size = 5) Pageable pageable, @PathVariable("id") Integer id, Model model) {
        Member member = adminService.findMemberById(id);
        Page<Product> myProductList = adminService.getProductByMemberIdOrderByCreatedAtDesc(pageable, id);
        model.addAttribute("member", member);
        model.addAttribute("myProductList", myProductList);
        return "admin/member/admin-member-detail";
    }

    //TODO 페이징 다시
    @GetMapping("/member-detail/transactions/{id}")
    public String getMemberTransDetail(@PageableDefault(size = 5) Pageable pageable, @PathVariable("id") Integer id, Model model) {
        Member member = adminService.findMemberById(id);
        Page<Product> myProductList = adminService.getProductByMemberIdOrderByCreatedAtDesc(pageable, id);
        model.addAttribute("member", member);
        model.addAttribute("myProductList", myProductList);
        return "admin/member/admin-member-trans-detail";
    }

    @GetMapping("/member-disable")
    public String getMemberDisable(Model model, @PageableDefault(size = 8) Pageable pageable) {
        Page<Member> memberList = adminService.getMemberList(pageable);
        model.addAttribute("memberList", memberList);
        return "admin/member/admin-member-disable";
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
    public String getProductsMonthly(Model model, @PageableDefault(size = 5) Pageable pageable) {
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
    public String postCategorySalesTop5() {
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

    @ResponseBody
    @PostMapping("/member-register-weekly")
    public String postMemberResisterWeekly() {
        List<AdminRegisterWeeklyResponse> adminRegisterWeeklyResponseList = adminService.findSignupWeeklyCounts();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminRegisterWeeklyResponse> it = adminRegisterWeeklyResponseList.iterator();

        while(it.hasNext()){
            AdminRegisterWeeklyResponse adminRegisterWeeklyResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            int count = adminRegisterWeeklyResponse.getSignupCount();
            Date SignupDate = adminRegisterWeeklyResponse.getSignupDate();

            jsonObject.addProperty("count", count);
            jsonObject.addProperty("SignupDate", String.valueOf(SignupDate));
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/product-month-totalSales")
    public String postProductMonthTotalSales() {
        List<AdminProductMonthTotalSalesResponse> productMonthTotalSalesResponsesList = adminService.findMonthTotalSales();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminProductMonthTotalSalesResponse> it = productMonthTotalSalesResponsesList.iterator();

        while(it.hasNext()){
            AdminProductMonthTotalSalesResponse productMonthTotalSalesResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            String month = productMonthTotalSalesResponse.getMonth();
            int total_sales = productMonthTotalSalesResponse.getTotal_sales();

            jsonObject.addProperty("month", month);
            jsonObject.addProperty("total_sales", total_sales);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @PostMapping("/process-action")
    public String postProcessAction(@RequestParam(value = "id", required = false) List<Integer> selectedIds,
                                @RequestParam("action") String action) {
        if (action.equals("alert")) {
            System.out.println("알림!");
        }
        return "redirect:/admin/member";
    }

    @PostMapping("/change-status/{id}")
    public String processAction(@PathVariable("id") Integer id,
                                @RequestParam("action") String action) {
        if (action.equals("able")) {
            System.out.println("회원 전환!");
            System.out.println("@@@@"+id);
        } else if (action.equals("disable")) {
            System.out.println("비회원 전환!");
        }
        return "redirect:/admin/member-disable";
    }

}