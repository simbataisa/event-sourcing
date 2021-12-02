package com.example.eventstore.storage.service.impl;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.storage.service.DomainEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.eventstore.Constants.KAFKA_BROKER;
import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;

@Slf4j
@RequiredArgsConstructor
@Profile("camel-kafka")
@Service
public class EventStorageBoardEventNotificationConsumer extends RouteBuilder implements Processor {

  public static final String ROUTE_ID = "my-event-storage-event-notification-consumer-route";
  private static final String CLIENT_ID = "my-event-storage-camel";

  private final ObjectMapper objectMapper;
  private final DomainEventService domainEventService;

  @Override
  public void configure() throws Exception {
    from(kafka(NOTIFICATION_TOPIC).clientId(CLIENT_ID)
                                  .brokers(KAFKA_BROKER)
                                  .keySerializer(StringSerializer.class.getName())
                                  .valueSerializer(JsonSerializer.class.getName())
                                  .getUri()).routeId(ROUTE_ID).process(this).end();
  }

  @Override
  public void process(Exchange exchange) throws JsonProcessingException {
    var body = exchange.getIn().getBody();
    if (Objects.nonNull(body)) {
      log.info("Raw body {}", body);
      final DomainEvent domainEvent = objectMapper.readValue(body.toString(), DomainEvent.class);
      this.domainEventService.processDomainEvent(domainEvent);
    } else {
      log.info("Exchange body is null");
    }
  }
}
