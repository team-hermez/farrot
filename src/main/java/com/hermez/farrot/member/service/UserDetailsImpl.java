package com.hermez.farrot.member.service;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final Member member;
    private final Role role;
    private final Integer id;
    private final String email;
    private final String password;

    public UserDetailsImpl(Member member, String email, String password) {
        this.member = member;
        this.role = member.getRole();
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getAuthority());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public Integer getId() { return id; }
}
