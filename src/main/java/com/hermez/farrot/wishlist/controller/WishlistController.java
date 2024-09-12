package com.hermez.farrot.wishlist.controller;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
import com.hermez.farrot.wishlist.entity.Wishlist;
import com.hermez.farrot.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;
    private final MemberRepository memberRepository;

    public WishlistController(WishlistService wishlistService, ProductService productService, MemberRepository memberRepository) {
        this.wishlistService = wishlistService;
        this.productService = productService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/add")
    public String addProductToWishlist(@RequestParam("productId") Integer productId, Model model) {
        Member member = memberRepository.getReferenceById(1);
        Product product = productService.getProductById(productId);
        wishlistService.addProductToWishlist(member, product);
        return "redirect:";
    }

    @PostMapping("/remove")
    public String removeProductFromWishlist(@RequestParam("productId") Integer productId, Model model) {
        Member member = memberRepository.getReferenceById(1);
        Product product = productService.getProductById(productId);
        wishlistService.removeProductFromWishlist(member, product);
        return "redirect:";
    }

    @GetMapping
    public String viewWishlist(Model model) {
        Member member = memberRepository.getReferenceById(1);
        List<Wishlist> wishlist = wishlistService.getMemberWishlist(member);
        model.addAttribute("wishlist", wishlist);
        return "wishlist/view";
    }
}