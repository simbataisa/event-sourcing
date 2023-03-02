package com.example.eventstore.query.endpoint;


import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.example.eventstore.query.service.BoardEventReactiveStream;
import com.example.eventstore.query.service.BoardQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
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

  private final BoardQueryService<Board, DomainEvent> service;
  private final ObjectMapper objectMapper;
  private final CamelReactiveStreamsService camelReactiveStreamsService;

  @MessageMapping("/my-event-store-query/rs/send-event")
  @SendTo("/topic/board-events")
  public String sentEvent(final String message) {
    log.info(message);
    return message;
  }

  @MessageMapping("/my-event-store-query/rs/boards")
  public Mono<String> board(String uuid) throws JsonProcessingException {
    log.info("uuid {}", uuid);
    return boardMono(UUID.fromString(uuid));
  }

  @MessageMapping("/my-event-store-query/rs/domain-event-stream")
  public Flux<String> domainEventStream() {
    return Flux.from(this.camelReactiveStreamsService.fromStream(
        BoardEventReactiveStream.STREAM_PATH,
        String.class
    ));
  }

  @MessageMapping("/my-event-store-query/rs/board-event-stream")
  public Flux<DomainEvent> boardEventStream(String boardUuid) {
    final UUID uuid = UUID.fromString(boardUuid);
    return Flux.from(this.camelReactiveStreamsService.fromStream(
        BoardEventReactiveStream.STREAM_PATH,
        String.class
    )).mapNotNull(s -> {
      try {
        return this.objectMapper.readValue(s, DomainEvent.class);
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
      }
      return null;
    }).filter(s -> s.getBoardUuid().equals(uuid));
  }

  private Mono<String> boardMono(UUID uuid) {
    return Mono.fromCallable(() -> {
      Optional<Board> optional = Optional.ofNullable(this.service.find(uuid));
      return optional.isPresent() ? this.objectMapper.writeValueAsString(optional.get()) : null;
    }).subscribeOn(Schedulers.boundedElastic());
  }
}
