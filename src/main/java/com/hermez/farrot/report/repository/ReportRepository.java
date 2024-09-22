package com.hermez.farrot.report.repository;

import com.hermez.farrot.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    boolean existsByProductIdAndMemberId(Integer productId, Integer memberId);
}
