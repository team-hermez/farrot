package com.hermez.farrot.index;

import com.hermez.farrot.product.dto.request.ProductSearchRequest;
import com.hermez.farrot.product.dto.response.ProductSearchResponse;
import com.hermez.farrot.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ProductService productService;

    public IndexController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/")
    public String index(Model model) {
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setSize(4);
        productSearchRequest.setPage(2);
        model.addAttribute("response", productService.getProductsByFilters(productSearchRequest));
        productSearchRequest.setSize(2);
        productSearchRequest.setPage(0);
        model.addAttribute("responseT", productService.getProductsByFilters(productSearchRequest));

        return "index/index";
    }
}
