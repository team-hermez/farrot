package com.hermez.farrot.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminCategorySalesTop5Response {
    private int count;
    private String categoryCode;
}
