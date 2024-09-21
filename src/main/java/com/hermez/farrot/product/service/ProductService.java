package com.hermez.farrot.product.service;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.product.dto.request.ProductSearchRequest;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.dto.response.ProductSearchResponse;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product, MultipartFile[] imageFiles);

    List<Category> getAllCategories();

    ProductDetailResponse getProductDetail(Integer productId);

    Product getProductById(Integer productId);

    void deleteProductById(Integer productId);

    void updateProductStatus(Integer productId, Integer statusId);

    List<Product> getProductsByMember(Integer memberId);

    Page<Product> searchProductsByName(String productName, Pageable pageable);

    ProductSearchResponse getProductsByFilters(ProductSearchRequest productSearchRequest);

    List<Product> findTop4Latest();

    void completeSale(Product product);
}
