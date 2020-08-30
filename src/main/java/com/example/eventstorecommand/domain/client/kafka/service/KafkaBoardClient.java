package com.example.eventstorecommand.domain.client.kafka.service;

import com.example.eventstorecommand.domain.client.BoardClient;
import com.example.eventstorecommand.domain.event.DomainEvent;
import com.example.eventstorecommand.domain.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;

import java.util.List;
import java.util.UUID;

import static com.example.eventstorecommand.domain.client.kafka.config.KafkaClientConfig.BOARD_EVENTS_SNAPSHOTS;


@Slf4j
public class KafkaBoardClient implements BoardClient {

  private final DomainEventSource domainEventSource;
  private final InteractiveQueryService interactiveQueryService;

  public KafkaBoardClient(final DomainEventSource domainEventSource, final InteractiveQueryService interactiveQueryService) {
    this.domainEventSource = domainEventSource;
    this.interactiveQueryService = interactiveQueryService;
  }

  @Override
  public void save(final Board board) {
    log.info("save : enter");

    List<DomainEvent> newChanges = board.changes();
    newChanges.forEach(domainEvent -> {
      log.info("save : domainEvent=" + domainEvent);

      this.domainEventSource.publish(domainEvent);
    });
    board.flushChanges();

    log.info("save : exit");
  }

  @Override
  public Board find(final UUID boardUuid) {
    log.info("find : enter");

    try {

      ReadOnlyKeyValueStore<String, Board> store = interactiveQueryService
          .getQueryableStore(BOARD_EVENTS_SNAPSHOTS, QueryableStoreTypes.keyValueStore());

      Board board = store.get(boardUuid.toString());
      if (null != board) {

        board.flushChanges();
        log.info("find : board=" + board.toString());

        log.info("find : exit");
        return board;

      } else {

        throw new IllegalArgumentException("board[" + boardUuid.toString() + "] not found!");
      }

    } catch (InvalidStateStoreException e) {
      log.error("find : error", e);
    }
    throw new IllegalArgumentException("board[" + boardUuid.toString() + "] not found!");
  }
}
