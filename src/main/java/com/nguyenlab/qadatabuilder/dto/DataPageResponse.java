package com.nguyenlab.qadatabuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class DataPageResponse<T> {

  @JsonProperty("code")
  private int code;
  @JsonProperty("data")
  private List<T> data;
  @JsonProperty("totalCount")
  private long totalCount;

  private DataPageResponse(int code, List<T> data, long totalCount) {
    this.code = code;
    this.data = data;
    this.totalCount = totalCount;
  }

  public static <T> DataPageResponse<T> create(int code, List<T> data, long totalCount) {
    return new DataPageResponse<T>(code, data, totalCount);
  }
}
