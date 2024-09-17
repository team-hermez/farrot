package com.hermez.farrot.admin.repository;

import com.hermez.farrot.admin.dto.AdminCategorySalesTop5Response;

import java.util.List;

public interface AdminRepositoryCustom {
    List<AdminCategorySalesTop5Response> findCategorySales();
}
