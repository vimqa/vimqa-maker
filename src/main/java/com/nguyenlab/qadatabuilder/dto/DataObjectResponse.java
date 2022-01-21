package com.nguyenlab.qadatabuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataObjectResponse<T> {

  @JsonProperty("code")
  private int code;
  @JsonProperty("data")
  private T data;

  private DataObjectResponse(int code, T data) {
    this.code = code;
    this.data = data;
  }

  public static <T> DataObjectResponse<T> create(int code, T data) {
    return new DataObjectResponse<T>(code, data);
  }
}
