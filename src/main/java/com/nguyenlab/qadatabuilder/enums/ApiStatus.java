package com.nguyenlab.qadatabuilder.enums;

import lombok.Getter;

@Getter
public enum ApiStatus {
  SUCCESS(1),
  FAIL(0);

  private final int code;

  ApiStatus(int code) {
    this.code = code;
  }
}
