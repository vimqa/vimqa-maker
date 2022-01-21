package com.nguyenlab.qadatabuilder.dto;

import java.util.List;
import lombok.Data;

@Data
public class ParagraphSampleResponse {

  private String titleA;
  private List<String> paragraphA;
  private String titleB;
  private List<String> paragraphB;

  private ParagraphSampleResponse(
      String titleA,
      List<String> paragraphA,
      String titleB,
      List<String> paragraphB
  ) {
    this.titleA = titleA;
    this.paragraphA = paragraphA;
    this.titleB = titleB;
    this.paragraphB = paragraphB;
  }

  public static ParagraphSampleResponse create(
      String titleA,
      List<String> paragraphA,
      String titleB,
      List<String> paragraphB
  ) {
    return new ParagraphSampleResponse(titleA, paragraphA, titleB, paragraphB);
  }
}
