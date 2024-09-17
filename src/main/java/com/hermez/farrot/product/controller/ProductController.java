package com.hermez.farrot.product.controller;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.image.dto.request.ImageRequest;
import com.hermez.farrot.image.service.ImageService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.dto.request.ProductSearchRequest;
import com.hermez.farrot.product.dto.request.ProductsSearchRequest;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.dto.response.ProductSearchResponse;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/product")
@Controller
public class ProductController {

    private final ProductService productService;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public ProductController(ProductService productService, MemberRepository memberRepository, ImageService imageService) {
        this.productService = productService;
        this.memberRepository = memberRepository;
        this.imageService = imageService;
    }

    @GetMapping("/products")
    public String getSearchProducts(@ModelAttribute ProductSearchRequest productSearchRequest, Model model) {
        List<Category> categories = productService.getAllCategories();
        ProductSearchResponse response = productService.getProductsByFilters(productSearchRequest);
        model.addAttribute("categories", categories);
        model.addAttribute("response", response);

        return "product/products";
    }

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam("productId") Integer productId, Model model) {
        ProductDetailResponse productDetailResponse = productService.getProductDetail(productId);
        model.addAttribute("productDetail", productDetailResponse);
        return "product/product-detail";
    }

    @GetMapping("/register-sell")
    public String getRegisterSell(Model model) {
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categories);
        return "product/register-sell";
    }

    @Transactional
    @PostMapping("/register-sell")
    public String postRegisterSell(@ModelAttribute Product product, @RequestParam("imageFiles") MultipartFile[] imageFiles , BindingResult result) {
        Member member = memberRepository.getReferenceById(1);
        product.setMember(member);
        productService.saveProduct(product);
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                System.out.println(imageFiles.toString());
                imageService.save(new ImageRequest<>(product, file));
            }
        }
        return "redirect:/product/products";
    }

    @GetMapping("/update-sell")
    public String getEditProduct(@RequestParam("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product/update-sell";
    }

    @PostMapping("/update-sell")
    public String updateProduct(@ModelAttribute Product product) {
        Member member = memberRepository.getReferenceById(1);
        product.setMember(member);
        productService.saveProduct(product);
        return "redirect:/product/product-detail?productId="+product.getId();
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer productId, Model model) {
        productService.deleteProductById(productId);
        return "redirect:/product/products";
    }

    @GetMapping("/search")
    public String getProducts(@ModelAttribute ProductsSearchRequest productsSearchRequest, Model model) {
        Pageable pageable = PageRequest.of(productsSearchRequest.getPage(), productsSearchRequest.getSize());
        Page<Product> productPage = productService.searchProductsByName(productsSearchRequest.getProductName(), pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", productsSearchRequest.getPage());
        model.addAttribute("size", productsSearchRequest.getSize());
        model.addAttribute("productName", productsSearchRequest.getProductName());

        return "product/search-products";
    }
}