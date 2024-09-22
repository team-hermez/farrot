package com.hermez.farrot.report.dto.request;

import com.hermez.farrot.report.entity.ReportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    private Integer productId;
    private Integer memberId;
    private String title;
    private String content;
    private ReportType reportType;
}
