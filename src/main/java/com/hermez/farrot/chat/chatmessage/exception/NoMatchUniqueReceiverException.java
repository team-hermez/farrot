package com.hermez.farrot.chat.chatmessage.exception;

public class NoMatchUniqueReceiverException extends RuntimeException {

  public NoMatchUniqueReceiverException() {
    super();
  }

  public NoMatchUniqueReceiverException(String message) {
    super(message);
  }

  public NoMatchUniqueReceiverException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoMatchUniqueReceiverException(Throwable cause) {
    super(cause);
  }
}
