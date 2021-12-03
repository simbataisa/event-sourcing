package com.example.eventstore.command.service;

import com.example.eventstore.Constants;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
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
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

import static com.example.eventstore.Constants.AGGREGATION_SNAPSHOT_VIEW;
import static com.example.eventstore.Constants.BOOTSTRAP_SERVER_ENV_KEY;
import static com.example.eventstore.Constants.CLOUD_IP_ADDRESS_ENV_KEY;
import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;
import static com.example.eventstore.Constants.SERVER_PORT_ENV_KEY;
import static com.example.eventstore.Constants.SNAPSHOT_TOPIC;

@Slf4j
@Data
@Profile("camel-kafka")
@Service
public class BoardEventNotificationKStreamProcessor implements DisposableBean {

  private static final String APPLICATION_ID = "my-event-store-command";
  private static final String CLIENT_ID = "mac-mini";
  private static final String IN_TOPIC = NOTIFICATION_TOPIC;
  private static final String MATERIALIZED_VIEW = AGGREGATION_SNAPSHOT_VIEW;
  private static final String OUT_TOPIC = SNAPSHOT_TOPIC;


  private final JsonSerde<DomainEvent> domainEventSerde;
  private final JsonSerde<Board> boardSerde;
  private final ObjectMapper objectMapper;

  private final KafkaAdmin kafkaAdmin;
  private final NewTopic notificationTopic;
  private final NewTopic snapshotTopic;
  private final NewTopic aggregationTopic;


  private final String bootstrapServers;
  private final String serverAddress;
  private final String serverPort;
  private final HostInfo hostInfo;
  private final KafkaStreams streams;

  public BoardEventNotificationKStreamProcessor(
      ObjectMapper objectMapper,
      KafkaAdmin kafkaAdmin,
      NewTopic notificationTopic,
      NewTopic snapshotTopic,
      NewTopic aggregationTopic,
      Environment environment
  ) throws UnknownHostException, SocketException {
    this.objectMapper = objectMapper;
    this.domainEventSerde = new JsonSerde<>(DomainEvent.class, objectMapper);
    this.boardSerde = new JsonSerde<>(Board.class, objectMapper);
    this.kafkaAdmin = kafkaAdmin;
    this.notificationTopic = notificationTopic;
    this.snapshotTopic = snapshotTopic;
    this.aggregationTopic = aggregationTopic;

    this.bootstrapServers = environment.getProperty(BOOTSTRAP_SERVER_ENV_KEY);
    this.serverAddress = environment.getProperty(CLOUD_IP_ADDRESS_ENV_KEY);
    this.serverPort = environment.getProperty(SERVER_PORT_ENV_KEY);
    this.hostInfo = new HostInfo(this.serverAddress, Integer.parseInt(Objects.requireNonNull(this.serverPort)));
    this.kafkaAdmin.createOrModifyTopics(notificationTopic, snapshotTopic, aggregationTopic);

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
    streamsConfiguration.put(
        StreamsConfig.APPLICATION_SERVER_CONFIG,
        String.format("%s:%d", hostInfo.host(), hostInfo.port())
    );
    streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, CLIENT_ID);
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    streamsConfiguration.put(JsonDeserializer.TRUSTED_PACKAGES, Constants.TRUSTED_PACKAGES);
    streamsConfiguration.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        Serdes
            .String()
            .getClass()
            .getName()
    );

    streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
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
    final KStream<String, DomainEvent> domainEventKStream = builder.stream(IN_TOPIC);
    var table = domainEventKStream
        .map((key, value) -> {
          log.info("key [{}] value [{}]", key, value);
          if (value != null) {
            return new KeyValue<>(value.getBoardUuid().toString(), value);
          }
          return null;
        })
        .groupBy((s, domainEvent) -> s, Grouped.with(Serdes.String(), this.domainEventSerde))
        .aggregate(
            Board::new,
            (key, domainEvent, board) -> board.handleEvent(domainEvent),
            Materialized
                .<String, Board, KeyValueStore<Bytes, byte[]>>as(MATERIALIZED_VIEW)
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
