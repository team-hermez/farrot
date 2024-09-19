package com.hermez.farrot.product.dto.response;

import com.hermez.farrot.image.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductDetailResponse {

    private Integer productId;

    private Integer sellerId;

    private String sellerName;

    private String categoryName;

    private String productName;

    private String description;

    private Integer price;

    private String productStatus;

    private String createdAt;

    private Integer view;

    private List<Image> images;
}
