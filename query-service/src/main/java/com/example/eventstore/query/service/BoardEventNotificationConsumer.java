package com.example.eventstore.query.service;

import com.example.eventstore.Constants;
import com.example.eventstore.event.BoardInitialized;
import com.example.eventstore.event.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;

@Slf4j
@Profile(value = {"default", "camel-kafka"})
@Service
public class BoardEventNotificationConsumer extends RouteBuilder implements Processor {

  public static final String ROUTE_ID = "my-event-store-query-event-notification-consumer-route";
  private static final String CLIENT_ID = "my-event-store-query-camel";

  private final JsonSerde<DomainEvent> domainEventSerde;
  private final ObjectMapper objectMapper;
  private final BoardQueryService boardService;

  private final String bootstrapServers;

  public BoardEventNotificationConsumer(
      JsonSerde<DomainEvent> domainEventSerde,
      ObjectMapper objectMapper,
      BoardQueryService boardService,
      Environment environment
  ) {
    this.domainEventSerde = domainEventSerde;
    this.objectMapper = objectMapper;
    this.boardService = boardService;
    this.bootstrapServers = environment.getProperty(Constants.BOOTSTRAP_SERVER_ENV_KEY);
  }


  @Override
  public void configure() throws Exception {
    from(kafka(NOTIFICATION_TOPIC).clientId(CLIENT_ID)
                                  .brokers(bootstrapServers)
                                  .keySerializer(StringSerializer.class.getName())
                                  .valueSerializer(JsonSerializer.class.getName())
                                  .getUri()).routeId(ROUTE_ID).process(this).end();
  }

  /*@Override
  public void process(Exchange exchange) {
    var body = exchange.getIn()
                       .getBody();
    if (Objects.nonNull(body)) {
      log.info("Raw body {}", body);
      final DomainEvent domainEvent = domainEventSerde.deserializer()
                                                      .deserialize(
                                                          NOTIFICATION_TOPIC,
                                                          DOMAIN_EVENT_SERDE_RECORD_HEADERS,
                                                          body.toString()
                                                              .getBytes(StandardCharsets.UTF_8)
                                                      );
      log.info(domainEvent.toString());
    } else {
      log.info("Exchange body is null");
    }
  }*/

  @Override
  public void process(Exchange exchange) throws JsonProcessingException {
    var body = exchange.getIn().getBody();
    if (Objects.nonNull(body)) {
      log.info("Raw body {}", body);
      final DomainEvent domainEvent = objectMapper.readValue(body.toString(), DomainEvent.class);
      processEvent(domainEvent);
    } else {
      log.info("Exchange body is null");
    }
  }

  private void processEvent(DomainEvent domainEvent) {
    if (domainEvent instanceof BoardInitialized) {
      log.info("No board should exist in cache if 'BoardInitialized' event is received");
    } else {
      try {
        log.info("uncacheTarget {} event {}", domainEvent.getBoardUuid(), domainEvent.eventType());
        this.boardService.uncacheTarget(domainEvent.getBoardUuid());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
