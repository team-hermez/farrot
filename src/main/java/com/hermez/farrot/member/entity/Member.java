package com.hermez.farrot.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "MEMBER")
@Getter
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

    @Column(nullable = false, length = 100)
    private String account;

    @Column(name="create_at", nullable = false, length = 100)
    private Date createAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 100)
    private String attributeCode;

    private String provider;

    public Member() {}

    // 기본 유저용 생성자
    @Builder
    public Member(String memberName, String email, String password, String phone, String nickname, String account, Date createAt, Role role) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.account = account;
        this.createAt = createAt;
        this.role = role;
    }

    //소셜 로그인용
    @Builder(builderMethodName = "toOAuth2")
    public Member(String memberName, String email, String password, String phone, String nickname, Date createAt,
            Role role, String provider, String attributeCode) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
        this.createAt = createAt;
        this.role = role;
        this.provider = provider;
        this.attributeCode = attributeCode;
    }
}
