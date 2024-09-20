package com.hermez.farrot.wishlist.entity;

import lombok.Getter;

@Getter
public enum WishType {
  WISH("WISH"),
  CANCEL("CANCEL"),
  NONE("NONE");

  private final String description;

  WishType(String description) {
    this.description = description;
  }

}
