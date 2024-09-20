package com.hermez.farrot.admin.service.impl;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.dto.AdminProductMonthTotalSalesResponse;
import com.hermez.farrot.admin.dto.AdminRegisterWeeklyResponse;
import com.hermez.farrot.admin.repository.AdminRepository;
import com.hermez.farrot.admin.service.AdminService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.product.repository.ProductStatusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    public final AdminRepository adminRepository;
    public final ProductRepository productRepository;
    public final ProductStatusRepository productStatusRepository;

    public AdminServiceImpl(AdminRepository adminRepository, ProductRepository productRepository, ProductStatusRepository productStatusRepository) {
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
        this.productStatusRepository = productStatusRepository;
    }

    public Page<Member> getMemberList(Pageable pageable){
        return adminRepository.findMemberByOrderById(pageable);
    }

    public int getMemberTotalCount(){
        return (int) adminRepository.count();
    }

    @Override
    public List<AdminCategorySalesTop5Response> getCategorySalesTop5() {
        return adminRepository.findCategorySales();
    }

    public List<AdminProductMonthTotalSalesResponse> findMonthTotalSales(){
        return adminRepository.findMonthTotalSales();
    }

    public Page<Product> getProductList(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Product> findProductsSoldRegisterToday(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return adminRepository.findProductsSoldRegisterToday(today, pageable);
    }

    public Member findMemberById(Integer memberId){
        Optional<Member> memberOptional = adminRepository.findById(memberId);
        return memberOptional.orElseThrow();
    }

    public int countByCreatedAtToday(){
        LocalDate today = LocalDate.now();
        return adminRepository.countByCreatedAtToday(today);
    }

    public List<AdminRegisterWeeklyResponse> findSignupWeeklyCounts(){
        return adminRepository.findSignupWeeklyCounts();
    }

    public Page<Product> getProductByMemberIdOrderByCreatedAtDesc(Pageable pageable, Integer memberId) {
        return productRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
    }

    public Page<Member> getMemberByStatusOrderById(int status, Pageable pageable) {
        return adminRepository.findMemberByStatusOrderById(status, pageable);
    }

    public List<Member> getMemberByStatus(int status){
        return adminRepository.findByStatus(status);
    }


    public void updateMemberDisableStatus(Integer memberId, String action) {
        if (action.equals("enable")){
            Member member= adminRepository.findById(memberId).get();
//            member.setStatus(1);
//            adminRepository.save(member);
        } else if(action.equals("disable")){
            Member member= adminRepository.findById(memberId).get();
//            member.setStatus(2);
//            adminRepository.save(member);
        }
    }
}
