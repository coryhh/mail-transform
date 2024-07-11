package com.arextest.mail.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class MailController {

  @Resource
  ObjectMapper objectMapper;

  private final String url = "http://mail.arextest.com//email/sendEmail";

  RestTemplate restTemplate;


  @PostConstruct
  void init() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(10000);
    requestFactory.setReadTimeout(10000);
    final int initialCapacity = 10;
    List<HttpMessageConverter<?>> httpMessageConverterList = new ArrayList<>(initialCapacity);
    httpMessageConverterList.add(new ByteArrayHttpMessageConverter());
    httpMessageConverterList.add(new StringHttpMessageConverter());
    httpMessageConverterList.add(new ResourceHttpMessageConverter());
    httpMessageConverterList.add(new SourceHttpMessageConverter<>());
    httpMessageConverterList.add(new AllEncompassingFormHttpMessageConverter());

    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
        objectMapper);
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
    httpMessageConverterList.add(converter);

    // set inner restTemplate
    restTemplate = new RestTemplate(httpMessageConverterList);
    restTemplate.setRequestFactory(requestFactory);
  }


  @PostMapping(value = "/email/sendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
  public String sendEmail(@RequestBody JSONObject object) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(object), headers);
    try {
      return restTemplate.postForObject(url, entity, String.class);
    } catch (Exception e) {
      log.error("send email error: {}", e.getMessage());
      return "";
    }
  }

}
