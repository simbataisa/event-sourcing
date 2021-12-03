package com.example.eventstore.command.config;

import com.example.eventstore.command.service.BoardEventNotificationKStreamProcessor;
import com.example.eventstore.service.impl.kafka.KStreamMetadataServiceImpl;
import com.example.eventstore.service.kafka.KStreamMetadataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ProtobufMessageConverter;

@Configuration
public class CommandConfig {

  @Bean
  public ProtobufMessageConverter protobufMessageConverter() {
    return new ProtobufMessageConverter();
  }

  @Bean
  public KStreamMetadataService kStreamMetadataService(BoardEventNotificationKStreamProcessor processor) {
    return new KStreamMetadataServiceImpl(processor.getStreams());
  }
}
