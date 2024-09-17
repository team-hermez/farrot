package com.hermez.farrot.wishlist.service;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.wishlist.entity.Wishlist;

import java.util.List;

public interface WishlistService {

    void addProductToWishlist(Member member, Product product);

    void removeProductFromWishlist(Member member, Product product);

    List<Wishlist> getMemberWishlist(Member member);
}
