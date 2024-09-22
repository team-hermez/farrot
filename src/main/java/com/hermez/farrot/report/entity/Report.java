package com.hermez.farrot.report.entity;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REPORT")

public class Report {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "REPORT_ID")
    private Integer id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Enumerated(STRING)
    private ReportType reportType;


}
