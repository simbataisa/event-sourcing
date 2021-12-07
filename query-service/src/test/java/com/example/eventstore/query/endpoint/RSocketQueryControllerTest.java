package com.example.eventstore.query.endpoint;

import com.example.eventstore.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CancellationException;

@Disabled("Comment this to test your rsocket connection")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RSocketQueryControllerTest {

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
  void boards() throws InterruptedException {
    rSocketRequester
        .route("/my-event-store-query/rs/board-events")
        .retrieveFlux(Board.class)
        .subscribe(System.out::println);
    while (true) {
      Thread.sleep(1000);
    }
  }
}
