package com.nguyenlab.qadatabuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class QaCreateRequest {

  @JsonProperty("question")
  private String question;
  @JsonProperty("answer")
  private String answer;
  @JsonProperty("firstTitle")
  private String firstTitle;
  @JsonProperty("firstParagraph")
  private List<String> firstParagraph;
  @JsonProperty("secondTitle")
  private String secondTitle;
  @JsonProperty("secondParagraph")
  private List<String> secondParagraph;
  @JsonProperty("firstSupportingFactIndex")
  private List<Integer> firstSupportingFactIndex;
  @JsonProperty("secondSupportingFactIndex")
  private List<Integer> secondSupportingFactIndex;
  @JsonProperty("annotator")
  private String annotator;
}
