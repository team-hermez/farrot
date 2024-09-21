package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductWeeklyTotalSalesResponse {
    String week;
    int total_sales;

    public AdminProductWeeklyTotalSalesResponse(String week, int totalSales) {
        this.week = week;
        this.total_sales = totalSales;
    }
}
