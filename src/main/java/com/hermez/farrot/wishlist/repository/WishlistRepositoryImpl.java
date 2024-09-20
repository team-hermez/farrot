package com.hermez.farrot.wishlist.repository;

import static com.hermez.farrot.wishlist.entity.QWishlist.wishlist;
import static com.hermez.farrot.wishlist.entity.WishType.WISH;

import com.hermez.farrot.wishlist.dto.WishlistDTO;
import com.hermez.farrot.wishlist.entity.Wishlist;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class WishlistRepositoryImpl implements WishlistRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public WishlistRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public Wishlist findOne(Integer productId, String userEmail) {
    return queryFactory
        .selectFrom(wishlist)
        .where(productIdEq(productId), memberEmailEq(userEmail))
        .fetchOne();
  }

  @Override
  public Wishlist findByMemberIdAndProductId(Integer memberId,Integer productId) {
    return queryFactory
        .selectFrom(wishlist)
        .where(MemberIdEq(memberId),productIdEq(productId))
        .fetchOne();
  }

  @Override
  public Optional<Integer> findCount(Integer memberId) {
    return Optional.of(queryFactory
        .selectFrom(wishlist)
        .where(MemberIdEq(memberId), wishlist.wishType.eq(WISH))
        .fetch()
        .size());
  }

  @Override
  public Page<WishlistDTO> findProductByMemberId(Integer memberId, Pageable pageable) {
    List<WishlistDTO> content = queryFactory
        .select(wishlist.product)
        .from(wishlist)
        .where(MemberIdEq(memberId), wishlist.wishType.eq(WISH))
        .fetch().stream().map(WishlistDTO::new).toList();
    int count = queryFactory
        .selectFrom(wishlist)
        .where(MemberIdEq(memberId), wishlist.wishType.eq(WISH))
        .fetch()
        .size();
    return new PageImpl<>(content, pageable, count);
  }

  @Override
  public List<WishlistDTO> findWishTop3ByMemberId(Integer memberId) {
    return queryFactory
        .select(wishlist.product)
        .from(wishlist)
        .where(MemberIdEq(memberId), wishlist.wishType.eq(WISH))
        .limit(3)
        .orderBy(wishlist.createdAt.desc())
        .fetch().stream().map(WishlistDTO::new).toList();
  }

  private BooleanExpression MemberIdEq(Integer memberId) {
    return wishlist.member.id.eq(memberId);
  }


  private BooleanExpression productIdEq(Integer productId) {
    return wishlist.product.id.eq(productId);
  }

  private BooleanExpression memberEmailEq(String memberEmail) {
    return wishlist.member.email.eq(memberEmail);
  }

}
