package com.example.eventstore.command.client.camel.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.example.eventstore.config.KafkaConfig.NOTIFICATION_TOPIC;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.direct;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;

@Profile("camel-kafka")
@Component
public class BoardEventNotificationPublishService extends RouteBuilder {

  public static final String ROUTE_ID = "my-event-store-command-camel-publish-route";
  private static final String CLIENT_ID = "my-event-store-command-camel";
  private static final String BROKER = "localhost:9092";

  @Override
  public void configure() throws Exception {
    from(direct(ROUTE_ID)).to(kafka(NOTIFICATION_TOPIC).clientId(CLIENT_ID).brokers(BROKER)).end();
  }
}
