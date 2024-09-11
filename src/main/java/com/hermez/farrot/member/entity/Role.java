package com.hermez.farrot.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("user");

    private final String description;
    Role(String description) {
        this.description = description;
    }
}
