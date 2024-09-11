package com.hermez.farrot.product.controller;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.dto.request.ProductsSearchRequest;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@Controller
public class ProductController {

    private final ProductService productService;
    private final MemberRepository memberRepository;

    public ProductController(ProductService productService, MemberRepository memberRepository) {
        this.productService = productService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/products")
    public String getProducts(@ModelAttribute ProductsSearchRequest productsSearchRequest, Model model) {
        List<Category> categories = productService.getAllCategories();
        Pageable pageable = PageRequest.of(productsSearchRequest.getPage(), productsSearchRequest.getSize());
        Page<Product> productPage = productService.getProductsByFilters(
                productsSearchRequest.getCategoryId(),
                productsSearchRequest.getMinPrice(),
                productsSearchRequest.getMaxPrice(),
                pageable
        );
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", productsSearchRequest.getPage());
        model.addAttribute("size", productsSearchRequest.getSize());
        model.addAttribute("categories", categories);

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

    @PostMapping("/register-sell")
    public String postRegisterSell(@ModelAttribute Product product, BindingResult result) {
        Member member = memberRepository.getReferenceById(1);
        product.setMember(member);
        productService.saveProduct(product);
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
}