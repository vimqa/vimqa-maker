package com.nguyenlab.qadatabuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenlab.qadatabuilder.dto.QaCreateRequest;
import com.nguyenlab.qadatabuilder.entity.QaSample;
import com.nguyenlab.qadatabuilder.entity.SupportParagraph;
import com.nguyenlab.qadatabuilder.repository.QaSampleRepository;
import com.nguyenlab.qadatabuilder.repository.SupportParagraphRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QaCreateService {

  private final QaSampleRepository qaSampleRepository;
  private final SupportParagraphRepository supportParagraphRepository;
  private final ObjectMapper objectMapper;

  public QaCreateService(
      QaSampleRepository qaSampleRepository,
      SupportParagraphRepository supportParagraphRepository,
      Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder
  ) {
    this.qaSampleRepository = qaSampleRepository;
    this.supportParagraphRepository = supportParagraphRepository;
    objectMapper = jackson2ObjectMapperBuilder.build();
  }

  @SneakyThrows
  public QaSample createQaSample(QaCreateRequest request) {
    SupportParagraph firstParagraph = SupportParagraph.create(
        request.getFirstTitle(),
        request.getFirstParagraph()
    );
    SupportParagraph secondParagraph = SupportParagraph.create(
        request.getSecondTitle(),
        request.getSecondParagraph()
    );
    firstParagraph = supportParagraphRepository.save(firstParagraph);
    secondParagraph = supportParagraphRepository.save(secondParagraph);
    QaSample qaSample = QaSample.create(
        request.getQuestion(),
        request.getAnswer(),
        firstParagraph,
        secondParagraph,
        request.getFirstSupportingFactIndex(),
        request.getSecondSupportingFactIndex(),
        request.getAnnotator()
    );
    qaSample = qaSampleRepository.save(qaSample);
    log.info("Finish creating QA sample, sample={}", objectMapper.writeValueAsString(qaSample));
    return qaSample;
  }
}
