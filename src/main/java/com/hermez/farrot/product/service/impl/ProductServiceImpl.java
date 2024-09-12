package com.hermez.farrot.product.service.impl;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.category.service.CategoryService;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.entity.ProductStatus;
import com.hermez.farrot.product.exception.ResourceNotFoundException;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.product.repository.ProductStatusRepository;
import com.hermez.farrot.product.service.ProductService;
import com.hermez.farrot.util.PriceFormatUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductStatusRepository productStatusRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ProductStatusRepository productStatusRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productStatusRepository = productStatusRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        ProductStatus defaultStatus = productStatusRepository.findByStatus("판매중")
                .orElseThrow(() -> new ResourceNotFoundException("판매중 상태 존재하지 않습니다."));
        product.setProductStatus(defaultStatus);
        return productRepository.save(product);
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Override
    public Page<Product> getProductsByFilters(Integer categoryId, Integer minPrice, Integer maxPrice, Pageable pageable) {
        return productRepository.findAll(
                (Specification<Product>) (root, query, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.conjunction();
                    if (categoryId != null) {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category").get("id"), categoryId));
                    }
                    if (minPrice != null) {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
                    }
                    if (maxPrice != null) {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
                    }
                    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
                    return predicate;
                }, pageable);
    }

    @Override
    public ProductDetailResponse getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));
        incrementViewCount(product);

        return ProductDetailResponse.builder()
                .productId(product.getId())
                .sellerId(product.getMember().getId())
                .sellerName(product.getMember().getMemberName())
                .categoryName(product.getCategory().getCode())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(PriceFormatUtil.formatPrice(product.getPrice()))
                .productStatus(product.getProductStatus().getStatus())
                .createdAt(product.getCreatedAt())
                .view(product.getView())
                .build();
    }

    private void incrementViewCount(Product product) {
        product.setView(product.getView() + 1);
        productRepository.save(product);
    }

    @Override
    public Product getProductById(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElseThrow(() -> new ResourceNotFoundException("상품이 존재하지 않습니다."));
    }

    @Override
    public void deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void updateProductStatus(Integer productId, Integer statusId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("상품이 없습니다."));
        ProductStatus status = productStatusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("상태가 없습니다."));

        product.setProductStatus(status);
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByMember(Integer memberId) {
        return productRepository.findByMemberId(memberId);
    }
}