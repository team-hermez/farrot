package com.hermez.farrot.product.service.impl;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.category.service.CategoryService;
import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.image.repository.ImageRepository;
import com.hermez.farrot.image.service.ImageService;
import com.hermez.farrot.product.dto.request.ProductSearchRequest;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.dto.response.ProductSearchResponse;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.entity.ProductStatus;
import com.hermez.farrot.product.exception.ResourceNotFoundException;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.product.repository.ProductStatusRepository;
import com.hermez.farrot.product.service.ProductService;
import com.hermez.farrot.util.DateUtils;
import com.hermez.farrot.util.PriceFormatUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductStatusRepository productStatusRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ProductStatusRepository productStatusRepository, ImageRepository imageRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productStatusRepository = productStatusRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
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
    public ProductSearchResponse getProductsByFilters(ProductSearchRequest request) {
        Pageable pageable = createPageRequest(request);
        Page<Product> productPage = fetchFilteredProducts(request, pageable);
        Map<Integer, List<Image>> productImages = fetchProductImages(productPage);

        return buildProductSearchResponse(request, productPage, productImages);
    }

    private Pageable createPageRequest(ProductSearchRequest request) {
        return PageRequest.of(request.getPage(), request.getSize());
    }

    private Page<Product> fetchFilteredProducts(ProductSearchRequest request, Pageable pageable) {
        return productRepository.findAll((Specification<Product>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.getProductName() != null && !request.getProductName().isEmpty()) {
                String productNamePattern = "%" + request.getProductName() + "%";
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("productName"), productNamePattern));
            }
            if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("category").get("id").in(request.getCategoryId()));
            }
            if (request.getMinPrice() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }
            if (request.getMemberId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("member").get("id"), request.getMemberId()));
            }
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return predicate;
        }, pageable);
    }

    private Map<Integer, List<Image>> fetchProductImages(Page<Product> productPage) {
        Map<Integer, List<Image>> productImages = new HashMap<>();
        for (Product product : productPage.getContent()) {
            List<Image> images = imageService.getImagesByEntity(product);
            if (images == null || images.isEmpty()) {
                Image defaultImage = Image.builder()
                        .path("/default-product-image.png")
                        .build();
                images.add(defaultImage);
            }
            productImages.put(product.getId(), images);
        }
        return productImages;
    }

    private ProductSearchResponse buildProductSearchResponse(ProductSearchRequest request, Page<Product> productPage, Map<Integer, List<Image>> productImages) {
        return ProductSearchResponse.builder()
                .productPage(productPage)
                .productImages(productImages)
                .currentPage(request.getPage())
                .size(request.getSize())
                .build();
    }

    @Override
    public ProductDetailResponse getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));
        incrementViewCount(product);
        List<Image> images =  imageService.getImagesByEntity(product);
        if(images.isEmpty()){
            Image defaultImage = new Image("/default-product-image.png");
            images.add(defaultImage);
        }
        return ProductDetailResponse.builder()
                .productId(product.getId())
                .sellerId(product.getMember().getId())
                .sellerName(product.getMember().getMemberName())
                .categoryName(product.getCategory().getCode())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .productStatus(product.getProductStatus().getStatus())
                .createdAt(DateUtils.timeAgoOrFormatted(product.getCreatedAt(), "yyyy-MM-dd HH:mm"))
                .view(product.getView())
                .images(images)
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

    @Override
    public Page<Product> searchProductsByName(String productName, Pageable pageable) {
        return productRepository.findByProductNameContainingIgnoreCase(productName, pageable);
    }

    @Override
    public List<Product> findTop5Latest() { return productRepository.findTop5ByOrderByCreatedAtDesc(); }
}