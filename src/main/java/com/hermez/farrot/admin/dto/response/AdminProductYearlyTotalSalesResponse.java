package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductYearlyTotalSalesResponse {
    String month;
    int total_sales;

    public AdminProductYearlyTotalSalesResponse(String month, int totalSales) {
        this.month = month;
        this.total_sales = totalSales;
    }
}
