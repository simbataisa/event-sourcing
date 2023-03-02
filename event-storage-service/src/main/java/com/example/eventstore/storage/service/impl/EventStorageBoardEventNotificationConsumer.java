package com.example.eventstore.storage.service.impl;

import com.example.eventstore.Constants;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.storage.service.DomainEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;

@Slf4j
@Profile({"camel-kafka-h2", "camel-kafka-rocksdb"})
@Service
public class EventStorageBoardEventNotificationConsumer extends RouteBuilder implements Processor {

  public static final String ROUTE_ID = "my-event-storage-event-notification-consumer-route";
  private static final String CLIENT_ID = "my-event-storage-camel";

  private final ObjectMapper objectMapper;
  private final DomainEventService domainEventService;
  private final String bootstrapServers;

  public EventStorageBoardEventNotificationConsumer(
      ObjectMapper objectMapper,
      DomainEventService domainEventService,
      Environment environment
  ) {
    this.objectMapper = objectMapper;
    this.domainEventService = domainEventService;
    this.bootstrapServers = environment.getProperty(Constants.BOOTSTRAP_SERVER_ENV_KEY);
    log.info("bootstrapServers {}", bootstrapServers);
  }

  @Override
  public void configure() throws Exception {
    from(kafka(NOTIFICATION_TOPIC).clientId(CLIENT_ID)
                                  .brokers(bootstrapServers)
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
