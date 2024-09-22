package com.hermez.farrot.report.entity;

import lombok.Getter;

@Getter
public enum ReportType {
    SCAM("가격 사기"),
    INSUFFICIENT_DESCRIPTION("상품 설명 미비"),
    DUPLICATE_POST("중복 게시물"),
    OTHER("기타");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }
}