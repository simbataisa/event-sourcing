package com.example.eventstore.command.service;

import com.example.eventstore.event.DomainEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.StaticEndpointBuilders;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;

@Profile("camel-kafka")
@Component
public class BoardEventNotificationPublisher extends RouteBuilder implements Processor {

  public static final String ROUTE_ID = "my-event-store-command-camel-publish-route";
  private static final String CLIENT_ID = "my-event-store-command-camel";

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Override
  public void configure() throws Exception {
    from(StaticEndpointBuilders.direct(ROUTE_ID))
        .log("BoardEventNotificationPublisher :: message body ${body}")
        .setHeader(KafkaConstants.KEY, simple("${body.boardUuid}"))
        //.process(this)
        .to(StaticEndpointBuilders
                .kafka(NOTIFICATION_TOPIC)
                .clientId(CLIENT_ID)
                .brokers(bootstrapServers)
                .keySerializer(StringSerializer.class.getName())
                .valueSerializer(JsonSerializer.class.getName())
        )
        .end();
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    exchange
        .getIn()
        .setHeader(
            KafkaConstants.KEY,
            exchange
                .getIn()
                .getBody(DomainEvent.class)
                .getBoardUuid()
        );
  }
}
