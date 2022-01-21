package com.nguyenlab.qadatabuilder.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class WikipediaClient {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final String WIKI_API_URL;

  public WikipediaClient(
      RestTemplateBuilder restTemplateBuilder,
      @Value("${application.wiki-language-code}") String wikiLanguageCode
  ) {
    this.objectMapper = new ObjectMapper();
    this.restTemplate = restTemplateBuilder.build();
    WIKI_API_URL = String.format("https://%s.wikipedia.org/w/api.php", wikiLanguageCode);
  }

  @SneakyThrows
  public ExtractIntro getExtractIntroByTitle(String title) {
    String url = UriComponentsBuilder.fromHttpUrl(WIKI_API_URL)
        .queryParam("action", "query")
        .queryParam("prop", "extracts")
        .queryParam("titles", title)
        .queryParam("explaintext", 1)
        .queryParam("exintro", 1)
        .queryParam("format", "json")
        .queryParam("redirects", 1)
        .build().toUriString();
    log.debug("Wikipedia query url={}", url);
    String strRes = restTemplate.getForObject(url, String.class);
    JsonNode jsonTree = objectMapper.readTree(strRes);
    jsonTree = jsonTree.path("query").path("pages");
    String pageKey = jsonTree.fieldNames().next();
    if (pageKey.equals("-1")) {
      throw new IllegalArgumentException("pages is -1");
    }
    JsonNode pageJson = jsonTree.get(pageKey);
    return new ExtractIntro(
        pageJson.get("title").asText(),
        pageJson.get("extract").asText()
    );
  }

  @SneakyThrows
  public String getWikiTextIntroFromTitle(String title) {
    String url = UriComponentsBuilder.fromHttpUrl(WIKI_API_URL)
        .queryParam("action", "parse")
        .queryParam("prop", "wikitext")
        .queryParam("page", title)
        .queryParam("section", 0)
        .queryParam("format", "json")
        .build().toUriString();
    log.debug("Wikipedia query url={}", url);
    String strRes = restTemplate.getForObject(url, String.class);
    JsonNode jsonTree = objectMapper.readTree(strRes);
    return jsonTree.path("parse").path("wikitext").path("*").asText();
  }

  @AllArgsConstructor
  @Getter
  public static class ExtractIntro {

    String title;
    String intro;
  }
}
