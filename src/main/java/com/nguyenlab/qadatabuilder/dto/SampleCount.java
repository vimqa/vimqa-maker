package com.nguyenlab.qadatabuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SampleCount {

  @JsonProperty("annotator")
  private String annotator;
  @JsonProperty("sampleCount")
  private long sampleCount;

  private SampleCount(String annotator, long sampleCount) {
    this.annotator = annotator;
    this.sampleCount = sampleCount;
  }

  public static SampleCount create(String annotator, long count) {
    return new SampleCount(annotator, count);
  }
}
