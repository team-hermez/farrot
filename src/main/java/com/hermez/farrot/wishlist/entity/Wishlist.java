package com.hermez.farrot.wishlist.entity;

import static com.hermez.farrot.wishlist.entity.WishType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wishlist")
public class Wishlist {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(STRING)
    private WishType wishType;

    private LocalDateTime createdAt;

    public static Wishlist createWishlist(Product product, Member member) {
        return Wishlist.builder()
            .product(product)
            .member(member)
            .wishType(WISH)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void changeCancel(){this.wishType = CANCEL;}

    public void changeWish() {this.wishType = WISH;}
}
