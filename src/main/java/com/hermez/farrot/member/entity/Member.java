package com.hermez.farrot.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@Builder(toBuilder = true)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Integer id;

    @Column(name="member_name", nullable = false, length = 100)
    private String memberName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = true, length = 100)
    private String account;

    @Column(name="create_at", nullable = false, length = 100)
    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 1)
    private int status;

    @Column(nullable = true)
    private int report;

    //소셜 로그인용 column
    @Column(nullable = true, length = 100)
    private String attributeCode;

    @Column(nullable = true, length = 100)
    private String provider;

    public Member() {}

    // 기본 유저용 생성자
    @Builder
    public Member(Integer id, String memberName, String email, String password, String phone, String nickname, String account,
                  LocalDateTime createAt, Role role, int status, int report,String attributeCode, String provider) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.account = account;
        this.createAt = createAt;
        this.role = role;
        this.status = status;
        this.report = report;
        this.attributeCode = attributeCode;
        this.provider = provider;
    }

    //소셜 로그인용
    @Builder(builderMethodName = "toOAuth2")
    public Member(String memberName, String email, String password, String phone, String nickname, LocalDateTime createAt,
            Role role, String provider, String attributeCode, int status) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.createAt = createAt;
        this.role = role;
        this.provider = provider;
        this.attributeCode = attributeCode;
        this.status = status;
    }
}
