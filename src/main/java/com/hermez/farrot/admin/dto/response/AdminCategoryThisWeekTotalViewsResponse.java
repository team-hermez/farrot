package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCategoryThisWeekTotalViewsResponse {
    String categoryCode;
    int total_views;

    public AdminCategoryThisWeekTotalViewsResponse(String categoryCode, int total_views) {
        this.categoryCode = categoryCode;
        this.total_views = total_views;
    }
}
