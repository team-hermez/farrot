package com.hermez.farrot.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsSearchRequest {
    private Integer categoryId;
    private Integer minPrice;
    private Integer maxPrice;
    private int page = 0;
    private int size = 8;
}
