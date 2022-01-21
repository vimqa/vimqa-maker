package com.nguyenlab.qadatabuilder.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "qa_sample")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class QaSample {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "question", nullable = false, length = 512)
  private String question;

  @Column(name = "answer", nullable = false, length = 512)
  private String answer;

  @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @JoinColumn(name = "first_paragraph_id", nullable = false)
  private SupportParagraph firstParagraph;

  @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @JoinColumn(name = "second_paragraph_id", nullable = false)
  private SupportParagraph secondParagraph;

  @ElementCollection
  @CollectionTable(name = "qa_sample_first_supporting_fact_index", joinColumns = @JoinColumn(name = "owner_id"))
  @Column(name = "first_supporting_fact_index")
  private List<Integer> firstSupportingFactIndex;

  @ElementCollection
  @CollectionTable(name = "qa_sample_second_supporting_fact_index", joinColumns = @JoinColumn(name = "owner_id"))
  @Column(name = "second_supporting_fact_index")
  private List<Integer> secondSupportingFactIndex;

  @Column(name = "annotator", nullable = false)
  private String annotator;

  private QaSample(
      String question,
      String answer,
      SupportParagraph firstParagraph,
      SupportParagraph secondParagraph,
      List<Integer> firstSupportingFactIndex,
      List<Integer> secondSupportingFactIndex,
      String annotator
  ) {
    this.question = question;
    this.answer = answer;
    this.firstParagraph = firstParagraph;
    this.secondParagraph = secondParagraph;
    this.firstSupportingFactIndex = firstSupportingFactIndex;
    this.secondSupportingFactIndex = secondSupportingFactIndex;
    this.annotator = annotator;
  }

  public static QaSample create(
      String question,
      String answer,
      SupportParagraph firstParagraph,
      SupportParagraph secondParagraph,
      List<Integer> firstSupportingFactIndex,
      List<Integer> secondSupportingFactIndex,
      String annotator
  ) {
    return new QaSample(
        question,
        answer,
        firstParagraph,
        secondParagraph,
        firstSupportingFactIndex,
        secondSupportingFactIndex,
        annotator
    );
  }
}