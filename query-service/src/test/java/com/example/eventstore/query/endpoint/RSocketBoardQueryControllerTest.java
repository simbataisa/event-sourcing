package com.example.eventstore.query.endpoint;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CancellationException;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RSocketBoardQueryControllerTest {

  private static RSocketRequester rSocketRequester;

  @BeforeAll
  private static void init() {
    RSocketRequester.Builder builder = RSocketRequester.builder();
    rSocketRequester = builder
        .rsocketStrategies(rsocketStrategies())
        .rsocketConnector(rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
        .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
        .websocket(URI.create("ws://localhost:9981/rs"));
  }

  private static RSocketStrategies rsocketStrategies() {
    return RSocketStrategies.builder()
                            .decoder(StringDecoder.textPlainOnly(), new Jackson2JsonDecoder())
                            .encoder(CharSequenceEncoder.allMimeTypes())
                            .dataBufferFactory(new DefaultDataBufferFactory(true))
                            .build();
  }


  @Test
  void board() {
    Hooks.onErrorDropped(e -> {
      if (e instanceof CancellationException || e.getCause() instanceof CancellationException) {
        log.warn("Operator called default onErrorDropped", e);
      } else {
        log.warn("Operator called default onErrorDropped", e);
      }
    });
    final String uuid = "60d3c82e-d9ec-4dd4-9709-dd28fe01966d";
    var result = rSocketRequester.route("/my-event-store-query/rs/boards")
                                 .data(uuid)
                                 .retrieveMono(Board.class);
    StepVerifier.create(result).consumeNextWith(i -> {
      log.info("Received board {}", i);
      Assertions.assertEquals(uuid, i.getBoardUuid().toString());
      Assertions.assertEquals("New Board", i.getName());
    }).verifyComplete();

  }

  @Test
  void domainEvents() throws InterruptedException {
    rSocketRequester
        .route("/my-event-store-query/rs/domain-event-stream")
        .retrieveFlux(DomainEvent.class)
        .subscribe(i -> log.info(i.toString()));
    while (true) {
      Thread.sleep(1000);
    }
  }

  @Test
  void boardEventStream() throws InterruptedException {
    final String uuid = "e978fec4-1567-4b06-a459-98a0444c397a";
    rSocketRequester
        .route("/my-event-store-query/rs/board-event-stream")
        .data(uuid)
        .retrieveFlux(DomainEvent.class)
        .subscribe(i -> log.info(i.toString()));
    while (true) {
      Thread.sleep(1000);
    }
  }
}
