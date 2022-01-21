package com.nguyenlab.qadatabuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompactQaSample {

  @JsonProperty("id")
  private Long id;
  @JsonProperty("question")
  private String question;
  @JsonProperty("answer")
  private String answer;
  @JsonProperty("firstTitle")
  private String firstTitle;
  @JsonProperty("secondTitle")
  private String secondTitle;
  @JsonProperty("annotator")
  private String annotator;

  private CompactQaSample(
      Long id,
      String question,
      String answer,
      String firstTitle,
      String secondTitle,
      String annotator
  ) {
    this.id = id;
    this.question = question;
    this.answer = answer;
    this.firstTitle = firstTitle;
    this.secondTitle = secondTitle;
    this.annotator = annotator;
  }

  public static CompactQaSample create(
      Long id,
      String question,
      String answer,
      String firstTitle,
      String secondTitle,
      String annotator
  ) {
    return new CompactQaSample(id, question, answer, firstTitle, secondTitle, annotator);
  }
}
