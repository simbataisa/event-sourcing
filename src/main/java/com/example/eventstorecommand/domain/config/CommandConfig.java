package com.example.eventstorecommand.domain.config;

import com.example.eventstorecommand.domain.client.BoardClient;
import com.example.eventstorecommand.domain.service.BoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ProtobufJsonFormatMessageConverter;
import org.springframework.messaging.converter.ProtobufMessageConverter;

@Configuration
public class CommandConfig {

  @Bean
  public BoardService boardService(final BoardClient boardClient) {
    return new BoardService(boardClient);
  }

  @Bean
  public ProtobufMessageConverter protobufMessageConverter() {
    return new ProtobufMessageConverter();
  }
}
