package com.hermez.farrot.product.dto.response;

import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class ProductSearchResponse {

    private Page<Product> productPage;

    private Map<Integer, List<Image>> productImages;

    private int currentPage;

    private int size;
}
