package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCategoryAveragePriceResponse {
    int averagePrice;
    String categoryCode;

    public AdminCategoryAveragePriceResponse(int averagePrice, String categoryCode) {
        this.averagePrice = averagePrice;
        this.categoryCode = categoryCode;
    }
}
