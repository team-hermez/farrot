package com.hermez.farrot.wishlist.repository;

import com.hermez.farrot.wishlist.dto.WishlistDTO;
import com.hermez.farrot.wishlist.entity.Wishlist;
import java.util.List;
import java.util.Optional;

public interface WishlistRepositoryCustom {

  Wishlist findOne(Integer productId, String userEmail);

  Wishlist findByMemberIdAndProductId(Integer memberId,Integer productId);

  Optional<Integer> findCount(Integer memberId);

  List<WishlistDTO> findProductByMemberId(Integer memberId);

  int findCountProductByMemberId(Integer memberId);

  List<WishlistDTO> findWishTop3ByMemberId(Integer memberId);
}
