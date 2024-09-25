package com.hermez.farrot.wishlist.controller;

import com.hermez.farrot.chat.chatroom.repository.ChatRoomRepositoryCustom;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.exception.NotFoundMemberException;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.notification.service.NotificationService;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
import com.hermez.farrot.wishlist.dto.WishlistDTO;
import com.hermez.farrot.wishlist.dto.request.WishRequest;
import com.hermez.farrot.wishlist.dto.response.WishResponse;
import com.hermez.farrot.wishlist.exception.BadWishRequestException;
import com.hermez.farrot.wishlist.service.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@Slf4j
public class WishlistController {

  private final WishlistService wishlistService;
  private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;
  private final ProductService productService;
  private final MemberRepository memberRepository;
  private final NotificationService notificationService;

  @ResponseBody
  @PostMapping("/wish")
  public WishResponse wish(@RequestBody WishRequest wishRequest,
      @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      throw new BadWishRequestException("찜기능 이용하시려면 로그인을 해주세요 \uD83D\uDC2D");}
    String userEmail = userDetails.getUsername();
    Member wishMember = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Member seller = chatRoomRepositoryCustom.findSellerByProductId(wishRequest.productId());
    Product wishProduct = productService.getProductById(wishRequest.productId());
    if (wishRequest.wishType().equals("NONE")) {
      wishlistService.save(wishRequest, userEmail);
      notificationService.createWishNotification(wishMember, seller, wishProduct);
      return new WishResponse("WISH");
    } else if (wishRequest.wishType().equals("WISH")) {
      wishlistService.changeCancel(wishRequest.productId(), userEmail);
      return new WishResponse("CANCEL");
    }
    wishlistService.changeWish(wishRequest.productId(), userEmail);
    notificationService.createWishNotification(wishMember, seller, wishProduct);
    return new WishResponse("WISH");
  }

  @ResponseBody
  @GetMapping("/count")
  public Integer count(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return 0;
    Integer result = wishlistService.findAll(userDetails.getUsername());
    log.info("Wishlist count: {}", result);
    return result == null ? 0 : result;
  }

  @GetMapping("/wishlist")
  public String wishlist(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable, Model model) {
    if (userDetails == null) return "redirect:/member/login";
    String userEmail = userDetails.getUsername();
    Page<WishlistDTO> wishProductPage = wishlistService.findProductByMemberId(userEmail, pageable);
    model.addAttribute("wishProductPage", wishProductPage);
    return "wishlist/wishlist";
  }

  @ResponseBody
  @GetMapping("/header-wish")
  public List<WishlistDTO> wishlist(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      throw new NotFoundMemberException("헤더 기능을 위한 사용자가 없습니다.");
    }
    String userEmail = userDetails.getUsername();
    return wishlistService.findWishTop3ByMemberId(userEmail);
  }
}
