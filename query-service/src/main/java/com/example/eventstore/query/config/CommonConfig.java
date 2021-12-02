package com.example.eventstore.query.config;

import com.example.eventstore.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.nio.charset.StandardCharsets;

import static org.springframework.kafka.support.converter.AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME;
import static org.springframework.kafka.support.converter.Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID;

@Configuration
public class CommonConfig {

  public static final RecordHeaders DOMAIN_EVENT_SERDE_RECORD_HEADERS = new RecordHeaders();

  static {
    DOMAIN_EVENT_SERDE_RECORD_HEADERS.add(new RecordHeader(
        DEFAULT_CLASSID_FIELD_NAME,
        DomainEvent.class.getName()
                         .getBytes(StandardCharsets.UTF_8)
    ));
    DOMAIN_EVENT_SERDE_RECORD_HEADERS.add(new RecordHeader(
        JsonDeserializer.TRUSTED_PACKAGES,
        "com.example.eventstore.event".getBytes(StandardCharsets.UTF_8)
    ));
  }

  @Bean
  public JsonSerde<DomainEvent> domainEventSerde(ObjectMapper objectMapper) {
    var domainEventJsonDeserializer = new JsonDeserializer<DomainEvent>(objectMapper);
    var typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.addTrustedPackages("com.example.eventstore.event");
    typeMapper.setTypePrecedence(TYPE_ID);
    domainEventJsonDeserializer.setTypeMapper(typeMapper);
    domainEventJsonDeserializer.dontRemoveTypeHeaders();

    var domainEventJsonSerializer = new JsonSerializer<DomainEvent>(objectMapper);
    domainEventJsonSerializer.setTypeMapper(typeMapper);

    return new JsonSerde<>(domainEventJsonSerializer, domainEventJsonDeserializer);
  }
}
