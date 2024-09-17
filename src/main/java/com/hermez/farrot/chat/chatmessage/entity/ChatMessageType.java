package com.hermez.farrot.chat.chatmessage.entity;

import lombok.Getter;

@Getter
public enum ChatMessageType {
  IMAGE("IMAGE"),
  TEXT("TEXT");

  private final String description;

  ChatMessageType(String description) {
    this.description = description;
  }

}
