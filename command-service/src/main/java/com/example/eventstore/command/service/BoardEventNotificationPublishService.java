package com.example.eventstore.command.service;

import com.example.eventstore.command.config.KafkaConfig;
import com.example.eventstore.event.DomainEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.StaticEndpointBuilders;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Profile("camel-kafka")
@Component
public class BoardEventNotificationPublishService extends RouteBuilder implements Processor {

  public static final String ROUTE_ID = "my-event-store-command-camel-publish-route";
  private static final String CLIENT_ID = "my-event-store-command-camel";
  private static final String BROKER = "localhost:9092";

  @Override
  public void configure() throws Exception {
    from(StaticEndpointBuilders.direct(ROUTE_ID))
        .process(this)
        .to(StaticEndpointBuilders
            .kafka(KafkaConfig.NOTIFICATION_TOPIC)
            .clientId(CLIENT_ID)
            .brokers(BROKER)
            .keySerializer(StringSerializer.class.getName())
            .valueSerializer(JsonSerializer.class.getName())
        )
        .end();
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    exchange.getIn().setHeader(KafkaConstants.KEY, exchange.getIn().getBody(DomainEvent.class).getBoardUuid());
    //exchange.getIn().setHeader(KafkaConstants.HEADERS, );
  }
}
