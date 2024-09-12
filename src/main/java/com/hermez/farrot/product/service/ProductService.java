package com.hermez.farrot.product.service;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    List<Category> getAllCategories();

    Page<Product> getProductsByFilters(Integer categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);

    ProductDetailResponse getProductDetail(Integer productId);

    Product getProductById(Integer productId);

    void deleteProductById(Integer productId);

    void updateProductStatus(Integer productId, Integer statusId);

    List<Product> getProductsByMember(Integer memberId);

}
