package com.hermez.farrot.oauth.service;

import com.hermez.farrot.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record PrincipalDetails(Member member, Map<String, Object> attributes, String attributesKey)
        implements OAuth2User, UserDetails {

    @Override
    public String getName() {
        return attributes.get(attributesKey).toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // 이메일을 사용자 이름으로 사용
    }

    @Override
    public String attributesKey() {
        return attributesKey;
    }
}
