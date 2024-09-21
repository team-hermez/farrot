package com.hermez.farrot.admin.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hermez.farrot.admin.dto.response.*;
import com.hermez.farrot.admin.dto.request.AdminNotificationRequest;
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
        int totalRegisterCount = adminService.countByCreatedAtToday();
        int totalSalesCount = adminService.countBySoldAtToday();
        System.out.println("totalSalesCount = " + totalSalesCount);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalRegisterCount", totalRegisterCount);
        model.addAttribute("totalSalesCount", Math.min(totalSalesCount, 367640));
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
    public String getProducts(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> productList = adminService.getProductList(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product";
    }

    @GetMapping("/products/today")
    public String getProductsToday(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> productList = adminService.findProductsRegisterToday(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product-today";
    }

    @GetMapping("/products/weekly")
    public String getProductsWeekly(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> productList = adminService.findProductsRegisterThisWeek(pageable);
        model.addAttribute("productList", productList);
        return "admin/product/admin-product-weekly";
    }

    @GetMapping("/products/monthly")
    public String getProductsMonthly(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> productList = adminService.findProductsRegisterThisMonth(pageable);
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
    public String postMemberRegisterWeekly() {
        List<AdminRegisterWeeklyResponse> adminRegisterWeeklyResponseList = adminService.findSignupWeeklyCounts();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminRegisterWeeklyResponse> it = adminRegisterWeeklyResponseList.iterator();

        while (it.hasNext()) {
            AdminRegisterWeeklyResponse adminRegisterWeeklyResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            int count = adminRegisterWeeklyResponse.getSignupCount();
            Date signupDate = adminRegisterWeeklyResponse.getSignupDate();

            jsonObject.addProperty("count", count);
            jsonObject.addProperty("SignupDate", String.valueOf(signupDate));
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/product-yearly-totalSales")
    public String postProductYearlyTotalSales() {
        List<AdminProductYearlyTotalSalesResponse> productYearlyTotalSalesResponseList = adminService.findYearlyTotalSales();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminProductYearlyTotalSalesResponse> it = productYearlyTotalSalesResponseList.iterator();

        while (it.hasNext()) {
            AdminProductYearlyTotalSalesResponse productYearlyTotalSalesResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            String month = productYearlyTotalSalesResponse.getMonth();
            int total_sales = productYearlyTotalSalesResponse.getTotal_sales();

            jsonObject.addProperty("month", month);
            jsonObject.addProperty("total_sales", total_sales);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/product-month-totalSales")
    public String postProductMonthlyTotalSales() {
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

    @ResponseBody
    @PostMapping("/product-week-totalSales")
    public String postProductWeeklyTotalSales() {
        List<AdminProductWeeklyTotalSalesResponse> productWeeklyTotalSalesResponsesList = adminService.findWeeklyTotalSales();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminProductWeeklyTotalSalesResponse> it = productWeeklyTotalSalesResponsesList.iterator();

        while (it.hasNext()) {
            AdminProductWeeklyTotalSalesResponse productWeeklyTotalSalesResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            String week = productWeeklyTotalSalesResponse.getWeek();
            int total_sales = productWeeklyTotalSalesResponse.getTotal_sales();

            jsonObject.addProperty("week", week);
            jsonObject.addProperty("total_sales", total_sales);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/product-hourly-totalSales")
    public String postProductTodayTotalSales() {
        List<AdminProductTodayTotalSalesResponse> productTodayTotalSalesResponseList = adminService.findTodayTotalSales();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminProductTodayTotalSalesResponse> it = productTodayTotalSalesResponseList.iterator();

        while (it.hasNext()) {
            AdminProductTodayTotalSalesResponse productTodayTotalSalesResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            String hours = productTodayTotalSalesResponse.getHours();
            int total_sales = productTodayTotalSalesResponse.getTotal_sales();

            jsonObject.addProperty("hours", hours);
            jsonObject.addProperty("total_sales", total_sales);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/member-monthly-register")
    public String findSignupMonthlyCounts() {
        List<AdminRegisterMonthlyResponse> registerMonthlyResponsesList = adminService.findSignupMonthlyCounts();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminRegisterMonthlyResponse> it = registerMonthlyResponsesList.iterator();

        while (it.hasNext()) {
            AdminRegisterMonthlyResponse registerMonthlyResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            int signup_count = registerMonthlyResponse.getSignup_count();
            String signupDate = registerMonthlyResponse.getSignupDate();

            jsonObject.addProperty("signup_count", signup_count);
            jsonObject.addProperty("signupDate", signupDate);

            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/category-view-many")
    public String postCategoryViewMany() {
        List<AdminCategoryThisWeekTotalViewsResponse> categoryThisWeekTotalViewsResponsesList = adminService.findThisWeekTotalViewsByCategory();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminCategoryThisWeekTotalViewsResponse> it = categoryThisWeekTotalViewsResponsesList.iterator();

        while (it.hasNext()) {
            AdminCategoryThisWeekTotalViewsResponse categoryThisWeekTotalViewsResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            int totalViews = categoryThisWeekTotalViewsResponse.getTotal_views();
            String categoryCode = categoryThisWeekTotalViewsResponse.getCategoryCode();

            jsonObject.addProperty("categoryCode", categoryCode);
            jsonObject.addProperty("totalViews", totalViews);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @ResponseBody
    @PostMapping("/category-price-average")
    public String postCategoryPriceAverage() {
        List<AdminCategoryAveragePriceResponse> categoryAveragePriceResponseList = adminService.findAveragePriceByCategory();
        Gson gson = new Gson();
        JsonArray jArray = new JsonArray();

        Iterator<AdminCategoryAveragePriceResponse> it = categoryAveragePriceResponseList.iterator();

        while (it.hasNext()) {
            AdminCategoryAveragePriceResponse categoryAveragePriceResponse = it.next();
            JsonObject jsonObject = new JsonObject();

            int averagePrice = categoryAveragePriceResponse.getAveragePrice();
            String categoryCode = categoryAveragePriceResponse.getCategoryCode();

            jsonObject.addProperty("categoryCode", categoryCode);
            jsonObject.addProperty("averagePrice", averagePrice);
            jArray.add(jsonObject);
        }
        String jsonString = gson.toJson(jArray);
        return jsonString;
    }

    @PostMapping("/process-action")
    public String postProcessAction(@ModelAttribute AdminNotificationRequest adminNotificationRequest) {
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