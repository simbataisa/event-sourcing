package com.example.eventstore.command.client;

import com.example.eventstore.command.client.camel.service.BoardEventNotificationKStreamProducerService;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.example.eventstore.command.client.camel.service.BoardEventNotificationPublishService.ROUTE_ID;
import static com.example.eventstore.common.Constants.CAMEL_DIRECT_ROUTE_PREFIX;
import static com.example.eventstore.config.KafkaConfig.SNAPSHOT_TOPIC;


@Slf4j
@Profile(value = {"camel-kafka"})
@Component
public class CamelKafkaBoardClient implements BoardClient<Board> {

  private final ProducerTemplate producerTemplate;
  private final CamelContext camelContext;
  private final ObjectMapper objectMapper;
  private final BoardEventNotificationKStreamProducerService boardEventNotificationKStreamProducerService;

  public CamelKafkaBoardClient(
      CamelContext camelContext,
      ObjectMapper objectMapper,
      BoardEventNotificationKStreamProducerService boardEventNotificationKStreamProducerService
  ) {
    this.camelContext = camelContext;
    this.producerTemplate = camelContext.createProducerTemplate();
    this.objectMapper = objectMapper;
    this.boardEventNotificationKStreamProducerService = boardEventNotificationKStreamProducerService;
  }

  @Override
  public void save(final Board board) {
    log.debug("save : enter");

    List<DomainEvent> newChanges = board.changes();
    newChanges.forEach(domainEvent -> {
      log.info("save : domainEvent = {}", domainEvent);
      var endpoint = this.camelContext.getEndpoint(CAMEL_DIRECT_ROUTE_PREFIX + ROUTE_ID);
      try {
        this.producerTemplate.asyncSendBody(endpoint, objectMapper.writeValueAsString(domainEvent));
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
      }
    });
    board.flushChanges();

    log.debug("save : exit");
  }

  @Override
  public Board find(final UUID boardUuid) {
    log.debug("find : enter");

    try {
      ReadOnlyKeyValueStore<String, Board> store = this.boardEventNotificationKStreamProducerService
          .getStreams()
          .store(StoreQueryParameters.fromNameAndType(SNAPSHOT_TOPIC, QueryableStoreTypes.keyValueStore()));

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
}
