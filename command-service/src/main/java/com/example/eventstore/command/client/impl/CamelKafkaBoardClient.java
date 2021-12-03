package com.example.eventstore.command.client.impl;

import com.example.eventstore.command.client.BoardClient;
import com.example.eventstore.command.service.BoardEventNotificationKStreamProcessor;
import com.example.eventstore.command.service.BoardEventNotificationPublisher;
import com.example.eventstore.common.util.CommonUtils;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.example.eventstore.service.kafka.KStreamMetadataService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.eventstore.Constants.AGGREGATION_SNAPSHOT_VIEW;
import static com.example.eventstore.Constants.CAMEL_DIRECT_ROUTE_PREFIX;
import static com.example.eventstore.Constants.CLOUD_IP_ADDRESS_ENV_KEY;
import static com.example.eventstore.Constants.SERVER_PORT_ENV_KEY;


@Slf4j
@Profile(value = {"camel-kafka"})
@Component
public class CamelKafkaBoardClient implements BoardClient<Board, DomainEvent> {

  private final ProducerTemplate producerTemplate;
  private final CamelContext camelContext;
  private final ReadOnlyKeyValueStore<String, Board> store;
  private final KStreamMetadataService kStreamMetadataService;
  private final String serverAddress;
  private final String serverPort;
  private final HostInfo hostInfo;

  public CamelKafkaBoardClient(
      CamelContext camelContext,
      BoardEventNotificationKStreamProcessor boardEventNotificationKStreamProcessor,
      KStreamMetadataService kStreamMetadataService,
      Environment environment
  ) {
    this.camelContext = camelContext;
    this.producerTemplate = camelContext.createProducerTemplate();
    this.store = boardEventNotificationKStreamProcessor
        .getStreams()
        .store(StoreQueryParameters.fromNameAndType(
            AGGREGATION_SNAPSHOT_VIEW,
            QueryableStoreTypes.keyValueStore()
        ));
    this.kStreamMetadataService = kStreamMetadataService;
    this.serverAddress = environment.getProperty(CLOUD_IP_ADDRESS_ENV_KEY);
    this.serverPort = environment.getProperty(SERVER_PORT_ENV_KEY);
    this.hostInfo = new HostInfo(this.serverAddress, Integer.parseInt(Objects.requireNonNull(this.serverPort)));
  }

  @Override
  public void save(final Board board) {
    log.debug("save : enter");

    List<DomainEvent> newChanges = board.changes();
    newChanges.forEach(domainEvent -> {
      log.info("save : domainEvent = {}", domainEvent);
      var endpoint = this.camelContext.getEndpoint(CAMEL_DIRECT_ROUTE_PREFIX + BoardEventNotificationPublisher.ROUTE_ID);
      this.producerTemplate.asyncSendBody(endpoint, domainEvent);
    });
    board.flushChanges();

    log.debug("save : exit");
  }

  @Override
  public Board find(final UUID boardUuid) {
    log.debug("find : enter");

    try {
      val host = kStreamMetadataService.streamsMetadataForStoreAndKey(
          AGGREGATION_SNAPSHOT_VIEW,
          boardUuid.toString(),
          Serdes.String().serializer()
      );
      Board board = null;
      log.info("host {}", host);
      log.info("hostInfo {}", hostInfo);
      if (CommonUtils.thisHost(host, this.hostInfo)) {
        log.info("thisHost = true");
        board = store.get(boardUuid.toString());
      } else {
        log.info("thisHost = false");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Board> responseEntity = restTemplate.getForEntity(
            "http://" + host.getHost() + ":" + host.getPort() + "/my-event-store-command/boards/" + boardUuid.toString(),
            Board.class
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
          board = responseEntity.getBody();
        }
      }

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
