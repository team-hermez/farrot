package com.hermez.farrot.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsSearchRequest {
    private String productName;
    private Integer page = 0;
    private Integer size = 10;
}