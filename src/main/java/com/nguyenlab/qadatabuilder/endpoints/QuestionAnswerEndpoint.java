package com.nguyenlab.qadatabuilder.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenlab.qadatabuilder.dto.CompactQaSample;
import com.nguyenlab.qadatabuilder.dto.DataObjectResponse;
import com.nguyenlab.qadatabuilder.dto.DataPageResponse;
import com.nguyenlab.qadatabuilder.dto.ParagraphSampleResponse;
import com.nguyenlab.qadatabuilder.dto.QaCreateRequest;
import com.nguyenlab.qadatabuilder.dto.QaCreateResponse;
import com.nguyenlab.qadatabuilder.dto.SampleCount;
import com.nguyenlab.qadatabuilder.entity.QaSample;
import com.nguyenlab.qadatabuilder.enums.ApiStatus;
import com.nguyenlab.qadatabuilder.service.ParagraphSamplingService;
import com.nguyenlab.qadatabuilder.service.QaCreateService;
import com.nguyenlab.qadatabuilder.service.QaDataService;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QuestionAnswerEndpoint {

  private final QaCreateService qaCreateService;
  private final QaDataService qaDataService;
  private final ParagraphSamplingService paragraphSamplingService;

  @PostMapping("/qa-sample")
  @CrossOrigin
  public QaCreateResponse postQaSample(@RequestBody QaCreateRequest request) {
    try {
      QaSample qaSample = qaCreateService.createQaSample(request);
      return QaCreateResponse.create(qaSample, ApiStatus.SUCCESS.getCode());
    } catch (Exception e) {
      log.error("Error handling create QA", e);
      return QaCreateResponse.create(null, ApiStatus.FAIL.getCode());
    }
  }

  @GetMapping("/qa-sample-compact")
  @CrossOrigin
  public DataPageResponse<CompactQaSample> getConciseArticleContexts(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "200") int size,
      @RequestParam(value = "sort", required = false) String[] sort
  ) {
    return qaDataService.findAllCompactQaSample(page, size, sort);
  }

  @GetMapping("/qa-sample/{id}")
  @CrossOrigin
  public DataObjectResponse<QaSample> getQaSampleById(@PathVariable long id) {
    return qaDataService.findOneQaSample(id);
  }

  @GetMapping("/qa-sample-count")
  @CrossOrigin
  public DataPageResponse<SampleCount> countSample() {
    return qaDataService.countSample();
  }

  @GetMapping("/sample/support-paragraph")
  @CrossOrigin
  public ParagraphSampleResponse sampleSupportParagraph() {
    return paragraphSamplingService.sampleSupportParagraphPair();
  }

  @SneakyThrows
  @GetMapping("/download-ds")
  public ResponseEntity<InputStreamResource> downloadDataset() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
    String nowStr = LocalDateTime.now().format(formatter);
    String fileName = "nguyenlab_qa_ds_" + nowStr;
    ObjectMapper mapper = new ObjectMapper();
    List<QaSample> qaSamples = qaDataService.findAllQaSample();
    byte[] buf = mapper.writeValueAsBytes(qaSamples);
    return ResponseEntity
        .ok()
        .header("Content-Disposition", "attachment; filename=\"" + fileName + ".json\"")
        .contentLength(buf.length)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(new ByteArrayInputStream(buf)));
  }
}
