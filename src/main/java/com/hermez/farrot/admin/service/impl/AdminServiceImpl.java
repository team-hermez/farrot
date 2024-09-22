package com.hermez.farrot.admin.service.impl;

import com.hermez.farrot.admin.dto.response.*;
import com.hermez.farrot.admin.repository.AdminRepository;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.product.repository.ProductStatusRepository;
import com.hermez.farrot.report.entity.Report;
import com.hermez.farrot.report.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    public final AdminRepository adminRepository;
    public final ProductRepository productRepository;
    public final ProductStatusRepository productStatusRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    public AdminServiceImpl(AdminRepository adminRepository, ProductRepository productRepository, ProductStatusRepository productStatusRepository, MemberRepository memberRepository, ReportRepository reportRepository) {
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
        this.productStatusRepository = productStatusRepository;
        this.memberRepository = memberRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    public Page<Member> getMemberList(Pageable pageable) {
        return adminRepository.findMemberByOrderById(pageable);
    }

    @Override
    public int getMemberTotalCount() {
        return (int) adminRepository.count();
    }

    @Override
    public List<AdminCategorySalesTop5Response> getCategorySalesTop5() {
        return adminRepository.findCategorySales();
    }

    @Override
    public List<AdminProductMonthTotalSalesResponse> findMonthTotalSales() {
        return adminRepository.findMonthTotalSales();
    }

    @Override
    public List<AdminProductYearlyTotalSalesResponse> findYearlyTotalSales() {
        return adminRepository.findYearlyTotalSales();
    }

    @Override
    public Page<Product> getProductList(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Product> findProductsRegisterToday(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return adminRepository.findProductsRegisterToday(today, pageable);
    }

    @Override
    public Page<Product> findProductsRegisterThisWeek(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return adminRepository.findProductsRegisteredThisWeek(today, pageable);
    }

    @Override
    public Page<Product> findProductsRegisterThisMonth(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return adminRepository.findProductsRegisteredThisMonth(today, pageable);
    }

    @Override
    public Member findMemberById(Integer memberId) {
        Optional<Member> memberOptional = adminRepository.findById(memberId);
        return memberOptional.orElseThrow();
    }

    @Override
    public int countByCreatedAtToday() {
        LocalDate today = LocalDate.now();
        return adminRepository.countByCreatedAtToday(today);
    }

    @Override
    public int countBySoldAtToday() {
        LocalDate today = LocalDate.now();
        return adminRepository.countSalesToday(today);
    }

    @Override
    public List<AdminProductWeeklyTotalSalesResponse> findWeeklyTotalSales() {
        return adminRepository.findWeeklyTotalSales();
    }

    @Override
    public List<AdminProductTodayTotalSalesResponse> findTodayTotalSales() {
        return adminRepository.findTodayTotalSales();
    }

    @Override
    public List<AdminCategoryThisWeekTotalViewsResponse> findThisWeekTotalViewsByCategory() {
        return adminRepository.findThisWeekTotalViewsByCategory();
    }

    @Override
    public Page<Report> getReportList(Pageable pageable){
            return reportRepository.findMemberByOrderById(pageable);
    }

    @Override
    public int countByReport(){
        return (int) reportRepository.count();
    }

    @Override
    public List<AdminRegisterWeeklyResponse> findSignupWeeklyCounts() {
        return adminRepository.findSignupWeeklyCounts();
    }

    @Override
    public List<AdminRegisterMonthlyResponse> findSignupMonthlyCounts() {
        return adminRepository.findSignupMonthlyCounts();
    }

    @Override
    public List<AdminCategoryAveragePriceResponse> findAveragePriceByCategory() {
        return adminRepository.findAveragePriceByCategory();
    }

    @Override
    public Page<Product> getProductByMemberIdOrderByCreatedAtDesc(Pageable pageable, Integer memberId) {
        return productRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
    }

    @Override
    public Page<Member> getMemberByStatusOrderById(int status, Pageable pageable) {
        return adminRepository.findMemberByStatusOrderById(status, pageable);
    }

    @Override
    public List<Member> getMemberByStatus(int status) {
        return adminRepository.findByStatus(status);
    }



    @Override
    public void updateMemberDisableStatus(Integer memberId, String action) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.get();
        if (action.equals("enable")) {
            member = member.toBuilder().status(1).build();

        } else if (action.equals("disable")) {
            member = member.toBuilder().status(2).build();
        }
        memberRepository.save(member);
    }


}
