package com.nguyenlab.qadatabuilder.service;

import com.nguyenlab.qadatabuilder.dto.CompactQaSample;
import com.nguyenlab.qadatabuilder.dto.DataObjectResponse;
import com.nguyenlab.qadatabuilder.dto.DataPageResponse;
import com.nguyenlab.qadatabuilder.dto.SampleCount;
import com.nguyenlab.qadatabuilder.entity.QaSample;
import com.nguyenlab.qadatabuilder.entity.SampleCountByAnnotator;
import com.nguyenlab.qadatabuilder.enums.ApiStatus;
import com.nguyenlab.qadatabuilder.mapper.QaSampleMapper;
import com.nguyenlab.qadatabuilder.repository.QaSampleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QaDataService {

  private final QaSampleRepository qaSampleRepository;

  public DataObjectResponse<QaSample> findOneQaSample(long id) {
    Optional<QaSample> find = qaSampleRepository.findById(id);
    if (find.isEmpty()) {
      log.info("QA sample not found, id={}", id);
      return DataObjectResponse.create(ApiStatus.FAIL.getCode(), null);
    }
    return DataObjectResponse.create(ApiStatus.SUCCESS.getCode(), find.get());
  }

  public DataPageResponse<SampleCount> countSample() {
    long totalCount = qaSampleRepository.count();
    List<SampleCountByAnnotator> sampleCountByAnnotators = qaSampleRepository.countSampleUsingAnnotator();
    List<SampleCount> data = new ArrayList<>();
    data.add(SampleCount.create("total", totalCount));
    for (SampleCountByAnnotator countByAnnotator : sampleCountByAnnotators) {
      data.add(SampleCount.create(
          countByAnnotator.getAnnotator(),
          countByAnnotator.getSampleCount()
      ));
    }
    return DataPageResponse.create(ApiStatus.SUCCESS.getCode(), data, 1);
  }

  public DataPageResponse<CompactQaSample> findAllCompactQaSample(
      int page,
      int size,
      String... sort
  ) {
    Pageable pageable = sort == null ?
        PageRequest.of(page, size) :
        PageRequest.of(page, size, Sort.by(sort));
    Page<QaSample> dataPage = qaSampleRepository.findAll(pageable);
    long count = qaSampleRepository.count();
    List<CompactQaSample> data = dataPage
        .stream()
        .map(QaSampleMapper::mapToCompactQaSample)
        .collect(Collectors.toList());
    return DataPageResponse.create(
        ApiStatus.SUCCESS.getCode(),
        data,
        count
    );
  }

  public List<QaSample> findAllQaSample() {
    return qaSampleRepository.findAll();
  }


}
