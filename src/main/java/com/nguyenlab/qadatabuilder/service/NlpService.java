package com.nguyenlab.qadatabuilder.service;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class NlpService {

  public final StanfordCoreNLP pipeline;

  public NlpService() {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit");
    this.pipeline = new StanfordCoreNLP(props);
  }

  public List<String> sentenceSplitting(String text) {
    List<String> sentences = new ArrayList<>();
    CoreDocument doc = new CoreDocument(text);
    pipeline.annotate(doc);
    for (CoreSentence sent : doc.sentences()) {
      sentences.add(sent.text());
    }
    return sentences;
  }

}
