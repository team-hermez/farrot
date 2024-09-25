package com.hermez.farrot.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("user"),
    ROLE_BLACKLIST("blacklist"),
    ROLE_ADMIN("admin");

    private final String description;
    Role(String description) {
        this.description = description;
    }

    public String getAuthority(){
        return this.name();
    }
}
