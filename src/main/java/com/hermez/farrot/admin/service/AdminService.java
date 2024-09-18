package com.hermez.farrot.admin.service;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.admin.dto.AdminRegisterWeeklyResponse;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    Page<Member> getMemberList(Pageable pageable);
    int getMemberTotalCount();
    Page<Product> getProductList(Pageable pageable);
    List<AdminCategorySalesTop5Response> getCategorySalesTop5();
    Page<Product> findProductsSoldToday(Pageable pageable);
    Member findMemberById(Integer memberId);
    int countByCreatedAtToday();
    List<AdminRegisterWeeklyResponse> findSignupWeeklyCounts();
//    Page<Product> getProductByMemberIdOrderByCreatedAtDesc(Pageable pageable, Integer memberId);
}
