package com.hermez.farrot.wishlist.service.impl;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.wishlist.entity.Wishlist;
import com.hermez.farrot.wishlist.repository.WishlistRepository;
import com.hermez.farrot.wishlist.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void addProductToWishlist(Member member, Product product) {
        if (wishlistRepository.findByMemberIdAndProductId(member.getId(), product.getId()).isEmpty()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setMember(member);
            wishlist.setProduct(product);
            wishlistRepository.save(wishlist);
        }
    }

    public void removeProductFromWishlist(Member member, Product product) {
        wishlistRepository.deleteByMemberIdAndProductId(member.getId(), product.getId());
    }

    public List<Wishlist> getMemberWishlist(Member member) {
        return wishlistRepository.findByMemberId(member.getId());
    }
}
