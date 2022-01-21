package com.nguyenlab.qadatabuilder.dto;

import com.nguyenlab.qadatabuilder.entity.QaSample;
import lombok.Data;

@Data
public class QaCreateResponse {

  private QaSample qaSample;
  private int status;

  private QaCreateResponse(QaSample qaSample, int status) {
    this.qaSample = qaSample;
    this.status = status;
  }

  public static QaCreateResponse create(QaSample qaSample, int status) {
    return new QaCreateResponse(qaSample, status);
  }
}
