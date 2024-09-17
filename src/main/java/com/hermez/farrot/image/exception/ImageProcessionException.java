package com.hermez.farrot.image.exception;

public class ImageProcessionException extends RuntimeException {

  public ImageProcessionException() {
    super();
  }

  public ImageProcessionException(String message) {
    super(message);
  }

  public ImageProcessionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImageProcessionException(Throwable cause) {
    super(cause);
  }
}