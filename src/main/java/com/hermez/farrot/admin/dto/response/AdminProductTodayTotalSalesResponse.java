package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductTodayTotalSalesResponse {
    String hours;
    int total_sales;

    public AdminProductTodayTotalSalesResponse(String hours, int totalSales) {
        this.hours = hours;
        this.total_sales = totalSales;
    }
}
