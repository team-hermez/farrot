package com.hermez.farrot.wishlist.service;

import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.wishlist.dto.WishlistDTO;
import com.hermez.farrot.wishlist.dto.request.WishRequest;
import com.hermez.farrot.wishlist.dto.response.WishResponse;
import com.hermez.farrot.wishlist.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistService {

  void save(WishRequest wishRequest, String userEmail);

  WishResponse findOne(String userEmail,Integer productId);

  void changeCancel(Integer productId, String userEmail);

  void changeWish(Integer productId, String userEmail);

  Integer findAll(String userEmail);

  Page<WishlistDTO> findProductByMemberId(String userEmail,Pageable pageable);

  default WishResponse entityToDto(Wishlist wishlist) {
    return WishResponse.builder()
        .wishType(wishlist.getWishType().getDescription())
        .build();
  }

  List<WishlistDTO> findWishTop3ByMemberId(String userEmail);
}
