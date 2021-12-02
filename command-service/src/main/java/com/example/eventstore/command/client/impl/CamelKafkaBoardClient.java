package com.example.eventstore.command.client.impl;

import com.example.eventstore.Constants;
import com.example.eventstore.command.client.BoardClient;
import com.example.eventstore.command.service.BoardEventNotificationKStreamProcessor;
import com.example.eventstore.command.service.BoardEventNotificationPublisher;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.example.eventstore.Constants.AGGREGATION_SNAPSHOT_VIEW;


@Slf4j
@Profile(value = {"camel-kafka"})
@Component
public class CamelKafkaBoardClient implements BoardClient<Board, DomainEvent> {

  private final ProducerTemplate producerTemplate;
  private final CamelContext camelContext;
  private final ReadOnlyKeyValueStore<String, Board> store;

  public CamelKafkaBoardClient(
      CamelContext camelContext,
      BoardEventNotificationKStreamProcessor boardEventNotificationKStreamProducerService
  ) {
    this.camelContext = camelContext;
    this.producerTemplate = camelContext.createProducerTemplate();
    this.store = boardEventNotificationKStreamProducerService
        .getStreams()
        .store(StoreQueryParameters.fromNameAndType(
            AGGREGATION_SNAPSHOT_VIEW,
            QueryableStoreTypes.keyValueStore()
        ));
  }

  @Override
  public void save(final Board board) {
    log.debug("save : enter");

    List<DomainEvent> newChanges = board.changes();
    newChanges.forEach(domainEvent -> {
      log.info("save : domainEvent = {}", domainEvent);
      var endpoint = this.camelContext.getEndpoint(Constants.CAMEL_DIRECT_ROUTE_PREFIX + BoardEventNotificationPublisher.ROUTE_ID);
      this.producerTemplate.asyncSendBody(endpoint, domainEvent);
    });
    board.flushChanges();

    log.debug("save : exit");
  }

  @Override
  public Board find(final UUID boardUuid) {
    log.debug("find : enter");

    try {
      final Board board = store.get(boardUuid.toString());
      if (null != board) {
        board.flushChanges();
        log.info("found : board=" + board);
        log.debug("find : exit");
        return board;

      } else {
        throw new IllegalArgumentException("board[" + boardUuid + "] not found!");
      }

    } catch (InvalidStateStoreException e) {
      log.error("find : error", e);
    }
    throw new IllegalArgumentException("board[" + boardUuid.toString() + "] not found!");
  }

  @Override
  public List<DomainEvent> getEvents(UUID boardUuid) {
    try {
      final Board board = store.get(boardUuid.toString());
      if (null != board) {
        return board.changes();

      } else {
        throw new IllegalArgumentException("board[" + boardUuid + "] not found!");
      }

    } catch (InvalidStateStoreException e) {
      log.error("find : error", e);
    }
    throw new IllegalArgumentException("board[" + boardUuid.toString() + "] not found!");
  }
}
