package com.example.eventstore.command.client.camel.service;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Properties;

import static com.example.eventstore.config.KafkaConfig.NOTIFICATION_TOPIC;
import static com.example.eventstore.config.KafkaConfig.SNAPSHOT_TOPIC;

@Slf4j
@Data
@Profile("camel-kafka")
@Service
public class BoardEventNotificationKStreamProducerService implements DisposableBean {

  private static final String APPLICATION_ID = "my-event-store-command";
  private static final String CLIENT_ID = "my-event-store-command-event-processor";
  private static final String BROKER = "localhost:9092";
  private static final String IN_TOPIC = NOTIFICATION_TOPIC;
  private static final String OUT_TOPIC = SNAPSHOT_TOPIC;
  private final KafkaStreams streams;

  private final JsonSerde<DomainEvent> domainEventSerde;
  private final JsonSerde<Board> boardSerde;
  private final ObjectMapper objectMapper;

  private final KafkaAdmin kafkaAdmin;

  public BoardEventNotificationKStreamProducerService(
      ObjectMapper objectMapper,
      KafkaAdmin kafkaAdmin,
      NewTopic notificationTopic,
      NewTopic snapshotTopic
  ) {
    this.objectMapper = objectMapper;
    this.domainEventSerde = new JsonSerde<>(DomainEvent.class, objectMapper);
    this.boardSerde = new JsonSerde<>(Board.class, objectMapper);
    this.kafkaAdmin = kafkaAdmin;
    this.kafkaAdmin.createOrModifyTopics(notificationTopic, snapshotTopic);
    // Configure the Streams application.
    final Properties streamsConfiguration = getStreamsConfiguration();

    // Define the processing topology of the Streams application.
    final StreamsBuilder builder = new StreamsBuilder();
    createStream(builder);
    this.streams = new KafkaStreams(builder.build(), streamsConfiguration);
    this.streams.cleanUp();
    this.streams.start();

  }

  private Properties getStreamsConfiguration() {
    final Properties streamsConfiguration = new Properties();
    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
    streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, CLIENT_ID);
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER);
    streamsConfiguration.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        Serdes
            .String()
            .getClass()
            .getName()
    );

    streamsConfiguration.put(
        StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        Serdes
            .String()
            .getClass()
            .getName()
    );
    streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
    streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
    return streamsConfiguration;
  }

  /**
   * Define the processing topology for Word Count.
   *
   * @param builder StreamsBuilder to use
   */
  private void createStream(final StreamsBuilder builder) {
    final KStream<String, String> domainEventKStream = builder.stream(IN_TOPIC);
    var table = domainEventKStream
        .map((key, value) -> {
          log.info("key [{}] value [{}]", key, value);
          if (value != null) {
            try {
              DomainEvent domainEvent = this.objectMapper.readValue(value, DomainEvent.class);
              return new KeyValue<>(domainEvent.getBoardUuid().toString(), domainEvent);
            } catch (JsonProcessingException e) {
              log.error(e.getMessage(), e);
            }

          }
          return null;
        })
        .groupBy((s, domainEvent) -> s, Grouped.with(Serdes.String(), this.domainEventSerde))
        .aggregate(
            Board::new,
            (key, domainEvent, board) -> board.handleEvent(domainEvent),
            Materialized
                .<String, Board, KeyValueStore<Bytes, byte[]>>as(OUT_TOPIC)
                .withKeySerde(Serdes.String())
                .withValueSerde(boardSerde)
        );
    table
        .toStream()
        .to(OUT_TOPIC, Produced.with(Serdes.String(), boardSerde));
  }

  @Override
  public void destroy() {
    if (this.streams != null) {
      log.info("Closing streams....");
      this.streams.close(Duration.ofSeconds(10));
    }
  }
}
