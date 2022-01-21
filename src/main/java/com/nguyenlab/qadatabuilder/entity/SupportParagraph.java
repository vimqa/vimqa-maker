package com.nguyenlab.qadatabuilder.entity;

import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "support_paragraph")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class SupportParagraph {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "title", nullable = false, length = 512)
  private String title;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "support_paragraph_sentences", joinColumns = @JoinColumn(name = "paragraph_id"))
  @Column(name = "sentence", nullable = false, length = 1024)
  private List<String> sentences;

  private SupportParagraph(String title, List<String> sentences) {
    this.title = title;
    this.sentences = sentences;
  }

  public static SupportParagraph create(String title, List<String> sentences) {
    return new SupportParagraph(title, sentences);
  }
}