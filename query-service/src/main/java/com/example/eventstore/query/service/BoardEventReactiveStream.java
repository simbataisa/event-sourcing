package com.example.eventstore.query.service;

import com.example.eventstore.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;

import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;
import static com.example.eventstore.query.config.WebSocketConfig.WS_BOARD_EVENT_TOPIC;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.kafka;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.reactiveStreams;

@Slf4j
@Service
public class BoardEventReactiveStream extends RouteBuilder {
  public static final String ROUTE_ID = "board-events-reactive-stream-route";
  private static final String CLIENT_ID = "esq-rs";

  public static final String STREAM_PATH = "board-events";

  private final String bootstrapServers;
  private final SimpMessagingTemplate template;
  private final CamelReactiveStreamsService camelReactiveStreamsService;


  public BoardEventReactiveStream(
      Environment environment,
      SimpMessagingTemplate template,
      CamelReactiveStreamsService camelReactiveStreamsService
  ) {
    this.bootstrapServers = environment.getProperty(Constants.BOOTSTRAP_SERVER_ENV_KEY);
    this.template = template;
    this.camelReactiveStreamsService = camelReactiveStreamsService;
  }

  @Override
  public void configure() throws Exception {
    from(kafka(NOTIFICATION_TOPIC).clientId(CLIENT_ID)
                                  .brokers(bootstrapServers)
                                  .keySerializer(StringSerializer.class.getName())
                                  .valueSerializer(StringSerializer.class.getName())
                                  .getUri()).routeId(ROUTE_ID).to(reactiveStreams(
        STREAM_PATH)).end();
  }

  @PostConstruct
  public void setupStreams() {

    Flux.from(this.camelReactiveStreamsService.fromStream(
            BoardEventReactiveStream.STREAM_PATH,
            String.class
        ))
        .doOnNext(this.broadWSCastEvents())
        .subscribe();
  }

  private Consumer<? super String> broadWSCastEvents() {
    return (Consumer<String>) s -> template.send(
        WS_BOARD_EVENT_TOPIC,
        new GenericMessage<>(s.getBytes(StandardCharsets.UTF_8))
    );
  }
}
