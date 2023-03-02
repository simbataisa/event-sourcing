package com.example.eventstore.storage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.JacksonUtils;

@Configuration
public class EventStorageCommonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    var om = JacksonUtils.enhancedObjectMapper();
    om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return om;
  }

}
