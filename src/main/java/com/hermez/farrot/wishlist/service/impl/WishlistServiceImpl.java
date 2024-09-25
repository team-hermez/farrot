package com.hermez.farrot.wishlist.service.impl;

import static com.hermez.farrot.wishlist.entity.WishType.NONE;

import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.image.repository.ImageRepository;
import com.hermez.farrot.image.service.ImageService;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.wishlist.dto.WishlistDTO;
import com.hermez.farrot.wishlist.dto.request.WishRequest;
import com.hermez.farrot.wishlist.dto.response.WishResponse;
import com.hermez.farrot.wishlist.entity.Wishlist;
import com.hermez.farrot.wishlist.repository.WishlistRepository;
import com.hermez.farrot.wishlist.service.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistServiceImpl implements WishlistService {

  private final WishlistRepository wishlistRepository;
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;
  private final ImageService imageService;

  @Transactional
  public void save(WishRequest wishRequest, String userEmail) {
    Product product = productRepository.findById((wishRequest.productId()))
        .orElseThrow(() -> new RuntimeException("Product not found"));
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    wishlistRepository.save(Wishlist.createWishlist(product, member));
  }

  @Override
  public WishResponse findOne(String userEmail,Integer productId) {
    Integer memberId = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Member not found")).getId();
    Wishlist wishlist = wishlistRepository.findByMemberIdAndProductId(memberId,productId);
    if (wishlist == null) {
      wishlist = Wishlist.builder().wishType(NONE).build();
    }
    return entityToDto(wishlist);
  }

  @Transactional
  @Override
  public void changeCancel(Integer productId, String userEmail) {
    wishlistRepository.findOne(productId, userEmail).changeCancel();
  }

  @Override
  @Transactional
  public void changeWish(Integer productId, String userEmail) {
    wishlistRepository.findOne(productId, userEmail).changeWish();
  }

  @Override
  public Integer findAll(String userEmail) {
    Integer memberId = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Member not found")).getId();
    return wishlistRepository.findCount(memberId).orElse(0);
  }

  @Override
  public Page<WishlistDTO> findProductByMemberId(String userEmail, Pageable pageable) {
    Integer memberId = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Member not found")).getId();

    List<WishlistDTO> content = wishlistRepository.findProductByMemberId(memberId).stream()
        .map(w ->
        {
          Product product = productRepository.findById(w.getId())
              .orElseThrow(() -> new RuntimeException("Product not found"));
          List<Image> images = imageService.getImagesByEntity(product);
          if(images.isEmpty()){
            Image defaultImage = new Image("/default-product-image.png");
            images.add(defaultImage);
          }
          return WishlistDTO.builder()
              .id(w.getId())
              .productName(w.getProductName())
              .price(w.getPrice())
              .description(w.getDescription())
              .productStatus(w.getProductStatus())
              .images(images)
              .build();
        }).toList();
    int count = wishlistRepository.findCountProductByMemberId(memberId);
    return new PageImpl<>(content, pageable, count);
  }

  @Override
  public List<WishlistDTO> findWishTop3ByMemberId(String userEmail) {
    Integer memberId = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Member not found")).getId();
    return wishlistRepository.findWishTop3ByMemberId(memberId).stream().map(w ->
        {
          Product product = productRepository.findById(w.getId())
              .orElseThrow(() -> new RuntimeException("Product not found"));
          List<Image> images = imageService.getImagesByEntity(product);
          if(images.isEmpty()){
            Image defaultImage = new Image("/default-product-image.png");
            images.add(defaultImage);
          }
          return WishlistDTO.builder()
              .id(w.getId())
              .productName(w.getProductName())
              .price(w.getPrice())
              .description(w.getDescription())
              .productStatus(w.getProductStatus())
              .latestImagePath(images.get(0).getPath())
              .build();
        }).toList();
  }
}
