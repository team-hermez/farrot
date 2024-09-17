package com.hermez.farrot.admin.repository;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AdminRepositoryCustom {
    List<AdminCategorySalesTop5Response> findCategorySales();
    Page<Product> findProductsSoldToday(LocalDate today, Pageable pageable);
}
