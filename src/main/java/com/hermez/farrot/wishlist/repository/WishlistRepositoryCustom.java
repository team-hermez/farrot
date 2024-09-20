package com.hermez.farrot.wishlist.repository;

import com.hermez.farrot.wishlist.dto.WishlistDTO;
import com.hermez.farrot.wishlist.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistRepositoryCustom {

  Wishlist findOne(Integer productId, String userEmail);

  Wishlist findByMemberIdAndProductId(Integer memberId,Integer productId);

  Optional<Integer> findCount(Integer memberId);

  Page<WishlistDTO> findProductByMemberId(Integer memberId,Pageable pageable);

  List<WishlistDTO> findWishTop3ByMemberId(Integer memberId);
}
