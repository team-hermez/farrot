package com.hermez.farrot.admin.service;

import com.hermez.farrot.admin.dto.response.*;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    Page<Member> getMemberList(Pageable pageable);
    int getMemberTotalCount();
    Page<Product> getProductList(Pageable pageable);
    List<AdminCategorySalesTop5Response> getCategorySalesTop5();
    Member findMemberById(Integer memberId);
    Page<Member> getMemberByStatusOrderById(int status, Pageable pageable);
    void updateMemberDisableStatus(Integer memberId, String action);

    int countByCreatedAtToday();
    int countBySoldAtToday();

    List<AdminRegisterWeeklyResponse> findSignupWeeklyCounts();
    List<AdminRegisterMonthlyResponse> findSignupMonthlyCounts();

    List<AdminCategoryAveragePriceResponse> findAveragePriceByCategory();

    Page<Product> getProductByMemberIdOrderByCreatedAtDesc(Pageable pageable, Integer memberId);
    List<Member> getMemberByStatus(int status);

    Page<Product> findProductsRegisterToday(Pageable pageable);
    Page<Product> findProductsRegisterThisWeek(Pageable pageable);
    Page<Product> findProductsRegisterThisMonth(Pageable pageable);

    List<AdminProductYearlyTotalSalesResponse> findYearlyTotalSales();
    List<AdminProductMonthTotalSalesResponse> findMonthTotalSales();
    List<AdminProductWeeklyTotalSalesResponse> findWeeklyTotalSales();
    List<AdminProductTodayTotalSalesResponse> findTodayTotalSales();

    List<AdminCategoryThisWeekTotalViewsResponse> findThisWeekTotalViewsByCategory();
}
