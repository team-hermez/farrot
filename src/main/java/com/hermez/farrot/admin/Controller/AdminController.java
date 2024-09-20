package com.hermez.farrot.admin.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.dto.AdminNotificationRequest;
import com.hermez.farrot.admin.dto.AdminProductMonthTotalSalesResponse;
import com.hermez.farrot.admin.dto.AdminRegisterWeeklyResponse;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.notification.service.NotificationService;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
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
    private ProductService productService;
    private NotificationService notificationService;

    public AdminController(AdminService adminService, ProductService productService, NotificationService notificationService) {
        this.adminService = adminService;
        this.productService = productService;
        this.notificationService = notificationService;
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
    public String getMemberDisable(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Member> memberList = adminService.getMemberByStatusOrderById(1, pageable);
        List<Member> disabledMemberList = adminService.getMemberByStatus(2);
        model.addAttribute("memberList", memberList);
        model.addAttribute("disabledMemberList", disabledMemberList);
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
        Page<Product> productList = adminService.findProductsSoldRegisterToday(pageable);
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

        while (it.hasNext()) {
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

        while (it.hasNext()) {
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

        while (it.hasNext()) {
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
    public String postProcessAction(@ModelAttribute AdminNotificationRequest adminNotificationRequest) {
        System.out.println("adminNotificationRequest.getSelectedIds() = " + adminNotificationRequest.getSelectedIds());
        System.out.println("adminNotificationRequest.getContent() = " + adminNotificationRequest.getContent());
        System.out.println("adminNotificationRequest.getDate() = " + adminNotificationRequest.getDate());
        System.out.println("adminNotificationRequest.getPriority() = " + adminNotificationRequest.getPriority());
        
        notificationService.creatNotification(adminNotificationRequest);
        return "redirect:/admin/member";
    }

    @GetMapping("/latest-products")
    @ResponseBody
    public List<Product> getLatestProducts() {
        return productService.findTop5Latest();
    }

    @PostMapping("/change-status/{id}")
    public String processAction(@PathVariable("id") Integer id,
                                @RequestParam("action") String action) {
        if (action.equals("enable")) {
            adminService.updateMemberDisableStatus(id, action);
            return "redirect:/admin/member-disable";
        } else if (action.equals("disable")) {
            adminService.updateMemberDisableStatus(id, action);
            return "redirect:/admin/member-disable";
        }
        return "redirect:/admin/member-disable";

    }

}