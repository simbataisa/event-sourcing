package com.example.eventstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ProtobufMessageConverter;

@Configuration
public class CommandConfig {

  @Bean
  public ProtobufMessageConverter protobufMessageConverter() {
    return new ProtobufMessageConverter();
  }
}
