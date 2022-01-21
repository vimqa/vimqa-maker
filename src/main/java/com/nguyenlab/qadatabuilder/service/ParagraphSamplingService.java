package com.nguyenlab.qadatabuilder.service;

import com.nguyenlab.qadatabuilder.client.WikipediaClient;
import com.nguyenlab.qadatabuilder.client.WikipediaClient.ExtractIntro;
import com.nguyenlab.qadatabuilder.dto.ParagraphSampleResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParagraphSamplingService {

  private final WikipediaClient wikipediaClient;
  private final NlpService nlpService;
  private final String[] preparedTitle;
  private final int MIN_PARAGRAPH_LENGTH = 256;

  @SneakyThrows
  public ParagraphSamplingService(
      WikipediaClient wikipediaClient,
      NlpService nlpService
  ) {
    this.wikipediaClient = wikipediaClient;
    this.nlpService = nlpService;
//    InputStream resource = new ClassPathResource("/prepared_titles.txt").getInputStream();
    InputStream resource = new ClassPathResource("/compare_titles.txt").getInputStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
      preparedTitle = reader.lines().toArray(String[]::new);
    }
  }

  private <T> T randomSample(T[] samples) {
    if (samples.length == 0) {
      throw new RuntimeException("Cannot random sample empty array");
    }
    int randomIdx = ThreadLocalRandom.current().nextInt(0, samples.length);
    return samples[randomIdx];
  }

  private String retryableGetRandomSampleTitle(String[] titleSamples) {
    final int MAX_RETRY = 3;
    int retryCount = 0;
    while (retryCount < MAX_RETRY) {
      try {
        String title = randomSample(titleSamples);
        if (!isValidTitle(title)) {
          throw new RuntimeException("Title is not valid");
        }
        return title;
      } catch (Exception e) {
        retryCount++;
      }
    }
    throw new RuntimeException("Random sample exceed max retry");
  }

  private boolean isNumber(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception ignored) {
      return false;
    }
  }

  private boolean isValidTitle(String title) {
    if (title.length() == 4 && isNumber(title)) {
      return false;
    }
    if (title.contains("định hướng")) {
      return false;
    }
    String lower = title.toLowerCase();
    if (lower.contains("ngày") || lower.contains("tháng") || lower.contains("năm")) {
      return false;
    }
    if (lower.contains("nobel")) {
      return false;
    }
    int countUncapitalized = 0;
    String[] split = title.split(" ");
    for (String word : split) {
      if (Character.isLowerCase(word.charAt(0))) {
        countUncapitalized++;
      }
    }
    return countUncapitalized < split.length - 1;
  }

  private List<String> extractHyperlinkTitle(String wikiText) {
    Pattern pattern = Pattern.compile("\\[\\[[^\\]]+]]");
    Matcher matcher = pattern.matcher(wikiText);
    List<String> hyperlinkTitle = new ArrayList<>();
    while (matcher.find()) {
      String text = matcher.group();
      text = text.substring(2, text.length() - 2);
      Collections.addAll(hyperlinkTitle, text.split("\\|"));
    }
    return hyperlinkTitle;
  }

  private String shortenIntro(String intro, int minLength) {
    String[] paragraphs = intro.split("\n");
    StringBuilder sb = new StringBuilder(paragraphs[0].trim());
    int paraIdx = 1;
    while (sb.length() < minLength && paraIdx < paragraphs.length) {
      sb.append(" ").append(paragraphs[paraIdx].trim());
      paraIdx++;
    }
    if (sb.length() < minLength) {
      throw new RuntimeException("Intro is too short");
    }
    return sb.toString();
  }

  private ParagraphSampleResponse trySampleSupportParagraphPair() {
    String randomTitle = randomSample(preparedTitle);
    ExtractIntro extractA = wikipediaClient.getExtractIntroByTitle(randomTitle);
    final String titleA = extractA.getTitle();
    String paragraphA = shortenIntro(extractA.getIntro(), MIN_PARAGRAPH_LENGTH);

    // Find related title for this paragraph
    String wikiText = wikipediaClient.getWikiTextIntroFromTitle(titleA);
    List<String> allHyperlinkTitle = extractHyperlinkTitle(wikiText);
    String[] relatedTitle = allHyperlinkTitle.stream()
        .filter(paragraphA::contains)
        .filter(this::isValidTitle)
        .filter(title -> !titleA.equals(title))
        .toArray(String[]::new);
    if (relatedTitle.length == 0) {
      throw new RuntimeException("No related title found for this paragraph");
    }
    String titleB = retryableGetRandomSampleTitle(relatedTitle);
    ExtractIntro extractB = wikipediaClient.getExtractIntroByTitle(titleB);
    titleB = extractB.getTitle();
    if (titleA.equals(titleB)) {
      throw new RuntimeException("The 2 paragraphs have the same title");
    }
    String paragraphB = shortenIntro(extractB.getIntro(), MIN_PARAGRAPH_LENGTH);
    List<String> sentencesInA = nlpService.sentenceSplitting(paragraphA);
    List<String> sentencesInB = nlpService.sentenceSplitting(paragraphB);
    return ParagraphSampleResponse.create(
        titleA,
        sentencesInA,
        titleB,
        sentencesInB
    );
  }

  private ParagraphSampleResponse trySampleSupportParagraphPairForComparison() {
    String randomA = randomSample(preparedTitle);
    ExtractIntro extractA = wikipediaClient.getExtractIntroByTitle(randomA);
    final String titleA = extractA.getTitle();
    String paragraphA = shortenIntro(extractA.getIntro(), MIN_PARAGRAPH_LENGTH);

    String randomB = randomSample(preparedTitle);
    if (randomB.equals(randomA)) {
      throw new RuntimeException("Sample the same title!");
    }
    ExtractIntro extractB = wikipediaClient.getExtractIntroByTitle(randomB);
    final String titleB = extractB.getTitle();
    String paragraphB = shortenIntro(extractB.getIntro(), MIN_PARAGRAPH_LENGTH);

    List<String> sentencesInA = nlpService.sentenceSplitting(paragraphA);
    List<String> sentencesInB = nlpService.sentenceSplitting(paragraphB);
    return ParagraphSampleResponse.create(
        titleA,
        sentencesInA,
        titleB,
        sentencesInB
    );
  }

  public ParagraphSampleResponse sampleSupportParagraphPair() {
    final int MAX_RETRY = 10;
    int retryCount = 0;
    while (retryCount < MAX_RETRY) {
      try {
        return trySampleSupportParagraphPair();
//        return trySampleSupportParagraphPairForComparison();
      } catch (Exception e) {
        log.error(
            "Error sample support paragraph, retryCount={}, reason={}",
            retryCount,
            e.getMessage(), e
        );
        retryCount++;
      }
    }
    throw new RuntimeException("Exceed max retry");
  }
}
