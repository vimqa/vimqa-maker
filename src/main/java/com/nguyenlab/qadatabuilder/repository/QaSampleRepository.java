package com.nguyenlab.qadatabuilder.repository;

import com.nguyenlab.qadatabuilder.entity.QaSample;
import com.nguyenlab.qadatabuilder.entity.SampleCountByAnnotator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QaSampleRepository extends JpaRepository<QaSample, Long> {

  @Query(value = "SELECT annotator, count(annotator) AS sampleCount FROM qa_sample GROUP BY annotator ORDER BY sampleCount DESC ", nativeQuery = true)
  List<SampleCountByAnnotator> countSampleUsingAnnotator();
}