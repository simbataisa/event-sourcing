package com.example.eventstorecommand.domain.client.kafka.service;

import com.example.eventstorecommand.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.Publisher;
import org.springframework.integration.config.EnablePublisher;
import org.springframework.messaging.support.MessageBuilder;

@Profile("kafka")
@EnableBinding(Source.class)
//@EnablePublisher //this Annotation + method @Publisher with return value will enable the AOP
@Slf4j
public class DomainEventSourceImpl implements DomainEventSource {

  private final Source source;
  private final ObjectMapper objectMapper;

  @Autowired
  public DomainEventSourceImpl(Source source, ObjectMapper objectMapper) {
    this.source = source;
    this.objectMapper = objectMapper;
  }


  //@Publisher(channel = Source.OUTPUT)
  public void publish(final DomainEvent event) {
    log.info("publishing event [{}]", event);
    source.output().send(MessageBuilder.withPayload(event).build());
  }
}
