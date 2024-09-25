package com.hermez.farrot.member.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class CustomUser extends User {
    private Member member;

    public CustomUser(Member member) {
        super(member.getEmail(), member.getPassword(), AuthorityUtils.createAuthorityList("MEMBER"));
        this.member = member;
    }
}
