package com.mollom.client;

public enum CaptchaType {
  IMAGE, AUDIO;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
