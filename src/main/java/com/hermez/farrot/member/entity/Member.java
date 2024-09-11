package com.hermez.farrot.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

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

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    public Member() {}

    @Builder
    public Member(String memberName, String email, String password, String phone, String nickname) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickname = nickname;
    }
}
