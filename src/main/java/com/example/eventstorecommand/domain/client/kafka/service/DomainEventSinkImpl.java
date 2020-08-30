package com.example.eventstorecommand.domain.client.kafka.service;

import com.example.eventstorecommand.domain.event.DomainEvent;
import com.example.eventstorecommand.domain.model.Board;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerde;

import static com.example.eventstorecommand.domain.client.kafka.config.KafkaClientConfig.BOARD_EVENTS_SNAPSHOTS;

@Profile("kafka")
@EnableBinding(BoardEventsStreamsProcessor.class)
@Slf4j
public class DomainEventSinkImpl implements DomainEventSink {

  private final ObjectMapper mapper;
  private final Serde<DomainEvent> domainEventSerde;
  private final Serde<Board> boardSerde;

  public DomainEventSinkImpl(final ObjectMapper mapper) {
    this.mapper = mapper;
    this.domainEventSerde = new JsonSerde<>(DomainEvent.class, mapper);
    this.boardSerde = new JsonSerde<>(Board.class, mapper);
  }

  @StreamListener(BoardEventsStreamsProcessor.INPUT)
  public void process(KStream<String, DomainEvent> input) {
    log.info("process : enter");
    log.info("input {}", input);
    input.map((key, value) -> {
      log.info("key [{}] value [{}]", key, value);
      if (value != null) {
        return new KeyValue<>(value.getBoardUuid().toString(), value);
      }
      return null;
    }).groupBy((s, domainEvent) -> s, Grouped.with(Serdes.String(), domainEventSerde)).aggregate(
        Board::new,
        (key, domainEvent, board) -> board.handleEvent(domainEvent),
        Materialized.<String, Board, KeyValueStore<Bytes, byte[]>>as(BOARD_EVENTS_SNAPSHOTS)
            .withKeySerde(Serdes.String())
            .withValueSerde(boardSerde)
    );
    log.info("process : exit");
  }

}
