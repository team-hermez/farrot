package com.hermez.farrot.report.service.impl;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.report.dto.request.ReportRequest;
import com.hermez.farrot.report.entity.Report;
import com.hermez.farrot.report.exception.DuplicateReportException;
import com.hermez.farrot.report.repository.ReportRepository;
import com.hermez.farrot.report.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ProductRepository productRepository) {
        this.reportRepository = reportRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void submitReport(ReportRequest reportRequest) {
        if (reportRepository.existsByProductIdAndMemberId(reportRequest.getProductId(), reportRequest.getMemberId())) {
            throw new DuplicateReportException("이미 신고한 상품입니다.");
        }

        Product product = productRepository.findById(reportRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        Report report = Report.builder()
                .product(product) // Product 객체 설정
                .member(Member.builder().id(reportRequest.getMemberId()).build())
                .title(reportRequest.getReportType().getDescription())
                .content(reportRequest.getContent())
                .createdAt(LocalDateTime.now())
                .reportType(reportRequest.getReportType())
                .build();

        reportRepository.save(report);
    }

}
