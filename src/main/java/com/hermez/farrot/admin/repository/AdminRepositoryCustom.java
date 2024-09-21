package com.hermez.farrot.admin.repository;

import com.hermez.farrot.admin.dto.response.*;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AdminRepositoryCustom {
    List<AdminCategorySalesTop5Response> findCategorySales();
    int countByCreatedAtToday(LocalDate today);
    int countSalesToday(LocalDate today);

    List<AdminRegisterWeeklyResponse> findSignupWeeklyCounts();
    List<AdminRegisterMonthlyResponse> findSignupMonthlyCounts();

    List<AdminProductYearlyTotalSalesResponse> findYearlyTotalSales();
    List<AdminProductTodayTotalSalesResponse> findTodayTotalSales();
    List<AdminProductWeeklyTotalSalesResponse> findWeeklyTotalSales();
    List<AdminProductMonthTotalSalesResponse> findMonthTotalSales();

    Page<Product> findProductsRegisterToday(LocalDate today, Pageable pageable);
    Page<Product> findProductsRegisteredThisWeek(LocalDate today, Pageable pageable);
    Page<Product> findProductsRegisteredThisMonth(LocalDate today, Pageable pageable);
}
