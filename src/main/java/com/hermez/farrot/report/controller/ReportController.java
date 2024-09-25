package com.hermez.farrot.report.controller;

import com.hermez.farrot.member.service.UserDetailsImpl;
import com.hermez.farrot.report.dto.request.ReportRequest;
import com.hermez.farrot.report.exception.DuplicateReportException;
import com.hermez.farrot.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/report")
    public String submitReport(@ModelAttribute ReportRequest reportRequest,
                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                               RedirectAttributes redirectAttributes) {
        if(userDetails == null) {
            return "redirect:/member/login";
        }
        Integer id = userDetails.getId();
        reportRequest.setMemberId(id);

        try {
            reportService.submitReport(reportRequest);
            redirectAttributes.addFlashAttribute("successMessage", "신고가 정상적으로 처리되었습니다.");
        } catch (DuplicateReportException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/product/product-detail?productId=" + reportRequest.getProductId();
        }

        return "redirect:/product/product-detail?productId=" + reportRequest.getProductId();
    }
}