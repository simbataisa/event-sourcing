package com.example.eventstorecommand.domain.client.kafka.config;

import com.example.eventstorecommand.domain.client.BoardClient;
import com.example.eventstorecommand.domain.client.kafka.service.DomainEventSource;
import com.example.eventstorecommand.domain.client.kafka.service.KafkaBoardClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Profile("kafka")
@Configuration
@EnableAutoConfiguration
@EnableKafkaStreams
public class KafkaClientConfig {

  public static final String BOARD_EVENTS_SNAPSHOTS = "board-events-snapshots";

  @Bean
  @Primary
  public BoardClient boardClient(final DomainEventSource domainEventSource, final InteractiveQueryService interactiveQueryService) {
    return new KafkaBoardClient(domainEventSource, interactiveQueryService);
  }

  /*@Bean
  public Function<KStream<String, DomainEvent>, KTable<String, Board>> processBoardEvents(ObjectMapper mapper) {
    final Serde domainEventSerde = new JsonSerde<>(DomainEvent.class, mapper);
    final Serde boardSerde = new JsonSerde<>(Board.class, mapper);
    return input -> input.map((key, value) -> {
      if (value != null) {
        return new KeyValue<>(value.getBoardUuid().toString(), value);
      }
      return null;
    }).groupBy((s, domainEvent) -> s, Grouped.with(Serdes.String(), domainEventSerde))
       .aggregate(
           Board::new,
           (key, domainEvent, board) -> ((Board) board).handleEvent(DomainEvent.class.cast(domainEvent)),
           Materialized.<String, Board, KeyValueStore<Bytes, byte[]>>as(BOARD_EVENTS_SNAPSHOTS)
               .withKeySerde(Serdes.String())
               .withValueSerde(boardSerde)
       );
  }

  @Bean
  @Primary
  public KafkaStreamsConfiguration defaultKafkaStreamsConfig(KafkaProperties kafkaProperties) {
    Map<String, Object> config = new HashMap<>();
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getClientId());
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new KafkaStreamsConfiguration(config);
  }*/
}
