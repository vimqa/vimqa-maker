package com.nguyenlab.qadatabuilder.mapper;

import com.nguyenlab.qadatabuilder.dto.CompactQaSample;
import com.nguyenlab.qadatabuilder.entity.QaSample;

public class QaSampleMapper {

  public static CompactQaSample mapToCompactQaSample(QaSample qaSample) {
    return CompactQaSample.create(
        qaSample.getId(),
        qaSample.getQuestion(),
        qaSample.getAnswer(),
        qaSample.getFirstParagraph().getTitle(),
        qaSample.getSecondParagraph().getTitle(),
        qaSample.getAnnotator()
    );
  }
}
