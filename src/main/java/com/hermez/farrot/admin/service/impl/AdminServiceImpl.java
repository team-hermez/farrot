package com.hermez.farrot.admin.service.impl;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
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
import java.util.List;

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

    public Page<Product> getProductList(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Product> findProductsSoldToday(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return adminRepository.findProductsSoldToday(today, pageable);
    }
}
