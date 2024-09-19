package com.hermez.farrot.admin.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductMonthTotalSalesResponse {
    private String month;
    private int total_sales;

    public AdminProductMonthTotalSalesResponse(String month, int totalSales) {
        this.month = month;
        this.total_sales = totalSales;
    }
}