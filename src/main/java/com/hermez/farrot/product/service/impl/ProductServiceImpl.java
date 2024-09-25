package com.hermez.farrot.product.service.impl;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.category.service.CategoryService;
import com.hermez.farrot.image.dto.request.ImageRequest;
import com.hermez.farrot.image.entity.Image;
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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductStatusRepository productStatusRepository;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ProductStatusRepository productStatusRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productStatusRepository = productStatusRepository;
        this.imageService = imageService;
    }

    @Transactional
    @Override
    public Product saveProduct(Product product, MultipartFile[] imageFiles) {
        ProductStatus defaultStatus = productStatusRepository.findByStatus("판매중")
                .orElseThrow(() -> new ResourceNotFoundException("판매중 상태 존재하지 않습니다."));
        List<Image> existingImages = imageService.findImagesByEntity(product.getId(), "PRODUCT");
        for (Image image : existingImages) {
            imageService.deleteImage(image);
        }
        product.setProductStatus(defaultStatus);
        productRepository.save(product);
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                imageService.save(new ImageRequest<>(product, file));
            }
        }
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

            predicate = addProductNameFilter(predicate, root, criteriaBuilder, request.getProductName());
            predicate = addCategoryFilter(predicate, root, criteriaBuilder, request.getCategoryId());
            predicate = addPriceFilter(predicate, root, criteriaBuilder, request.getMinPrice(), request.getMaxPrice());
            predicate = addMemberFilter(predicate, root, criteriaBuilder, request.getMemberId());

            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return predicate;
        }, pageable);
    }

    private Predicate addProductNameFilter(Predicate predicate, Root<Product> root, CriteriaBuilder criteriaBuilder, String productName) {
        if (productName != null && !productName.isEmpty()) {
            String productNamePattern = "%" + productName + "%";
            return criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("productName"), productNamePattern));
        }
        return predicate;
    }

    private Predicate addCategoryFilter(Predicate predicate, Root<Product> root, CriteriaBuilder criteriaBuilder, List<Integer> categoryId) {
        if (categoryId != null && !categoryId.isEmpty()) {
            return criteriaBuilder.and(predicate, root.get("category").get("id").in(categoryId));
        }
        return predicate;
    }

    private Predicate addPriceFilter(Predicate predicate, Root<Product> root, CriteriaBuilder criteriaBuilder, Integer minPrice, Integer maxPrice) {
        if (minPrice != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        return predicate;
    }

    private Predicate addMemberFilter(Predicate predicate, Root<Product> root, CriteriaBuilder criteriaBuilder, Integer memberId) {
        if (memberId != null) {
            return criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("member").get("id"), memberId));
        }
        return predicate;
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
        Product product = getProductById(productId);
        incrementViewCount(product);
        Product productForImage = Product.builder().id(product.getId()).build();
        List<Image> images =  imageService.getImagesByEntity(productForImage);
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
    @Transactional
    public Product getProductById(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElseThrow(() -> new ResourceNotFoundException("상품이 존재하지 않습니다."));
    }

    @Override
    @Transactional
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
        if(statusId==3){
            product.setSoldAt(LocalDateTime.now());
        }
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
    public List<Product> findTop4Latest() { return productRepository.findTop4ByOrderByCreatedAtDesc(); }

    @Override
    public void completeSale(Product product) {
        product.markAsSold();
        productRepository.save(product);
    }

}