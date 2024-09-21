package com.hermez.farrot.product.controller;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.category.service.CategoryService;
import com.hermez.farrot.member.security.JwtTokenProvider;
import com.hermez.farrot.member.service.MemberService;
import com.hermez.farrot.member.service.UserService;
import com.hermez.farrot.product.dto.request.ProductSearchRequest;
import com.hermez.farrot.product.dto.request.ProductsSearchRequest;
import com.hermez.farrot.product.dto.response.ProductDetailResponse;
import com.hermez.farrot.product.dto.response.ProductSearchResponse;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
import com.hermez.farrot.wishlist.dto.response.WishResponse;
import com.hermez.farrot.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/product")
@Controller
public class ProductController {

    private final ProductService productService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final MemberService memberService;
    private final WishlistService wishlistService;

    public ProductController(ProductService productService, JwtTokenProvider jwtTokenProvider, UserService userService, MemberService memberService,
        WishlistService wishlistService) {
        this.productService = productService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.memberService = memberService;
      this.wishlistService = wishlistService;
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
    public String getProductDetail(@RequestParam("productId") Integer productId,@AuthenticationPrincipal
        UserDetails userDetails,Model model) {
        WishResponse wishResponse = WishResponse.builder().build();
        if(userDetails != null) {
        String userEmail = userDetails.getUsername();
         wishResponse = wishlistService.findOne(userEmail,productId);
        }
        ProductDetailResponse productDetailResponse = productService.getProductDetail(productId);
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setSize(4);
        productSearchRequest.setPage(0);
        ProductSearchResponse response = productService.getProductsByFilters(productSearchRequest);
        model.addAttribute("wishResponse", wishResponse);
        model.addAttribute("productDetail", productDetailResponse);
        model.addAttribute("response", response);
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
    public String postRegisterSell(@ModelAttribute Product product, @RequestParam("imageFiles") MultipartFile[] imageFiles, BindingResult result, HttpServletRequest servletRequest) {
        product.setMember(memberService.getMember(servletRequest));
        productService.saveProduct(product, imageFiles);
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
    public String updateProduct(@ModelAttribute Product product, @RequestParam("imageFiles") MultipartFile[] imageFiles) {;
        productService.saveProduct(product, imageFiles);
        return "redirect:/product/product-detail?productId=" + product.getId();
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer productId) {
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

    @GetMapping("/member-products")
    public String getMemberProducts(@ModelAttribute ProductSearchRequest productSearchRequest, Model model, HttpServletRequest servletRequest) {
        productSearchRequest.setMemberId(userService.userDetail(jwtTokenProvider.resolveToken(servletRequest)).getId());
        ProductSearchResponse response = productService.getProductsByFilters(productSearchRequest);
        model.addAttribute("response", response);
        return "product/member-products";
    }
}