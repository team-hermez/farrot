package com.hermez.farrot.product.entity;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "product")
    private List<Payment> payments;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer view;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_status_id", nullable = false)
    private ProductStatus productStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.view=1;
    }
}

