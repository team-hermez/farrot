package com.hermez.farrot.product.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSearchRequest {

    private String productName;

    private List<Integer> categoryId;

    private Integer minPrice;

    private Integer maxPrice;

    private int page = 0;

    private int size = 8;
}
