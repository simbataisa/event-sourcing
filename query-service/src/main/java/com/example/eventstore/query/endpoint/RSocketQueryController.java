package com.example.eventstore.query.endpoint;


import com.example.eventstore.model.Board;
import com.example.eventstore.query.service.BoardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CancellationException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RSocketQueryController {

  static {
    /**
     * Workaround to solve the error on client dispose rsocket connection
     * rsocket-java github issue: https://github.com/rsocket/rsocket-java/issues/1018
     * Fix: https://github.com/rsocket/rsocket-java/commit/1f7191456f2961d1d29f682fb609c7d0783ef9a2
     */
    Hooks.onErrorDropped(e -> {
      if (e instanceof CancellationException || e.getCause() instanceof CancellationException) {
        log.warn("Operator called default onErrorDropped", e);
      } else {
        log.warn("Operator called default onErrorDropped", e);
      }
    });
  }

  private final BoardService service;
  private final ObjectMapper objectMapper;

  @MessageMapping("/my-event-store-query/rs/send-event")
  @SendTo("/topic/board-events")
  public String boardEvents(final String message) {
    log.info(message);
    return message;
  }

  @MessageMapping("/my-event-store-query/rs/boards")
  public Mono<String> board(String uuid) throws JsonProcessingException {
    log.info("uuid {}", uuid);
    return boardMono(UUID.fromString(uuid));
  }

  private Mono<String> boardMono(UUID uuid) {


    return Mono.fromCallable(() -> {
      Optional<Board> optional = Optional.ofNullable(this.service.find(uuid));
      return optional.isPresent() ? this.objectMapper.writeValueAsString(optional.get()) : null;
    }).subscribeOn(Schedulers.boundedElastic());
  }
}
