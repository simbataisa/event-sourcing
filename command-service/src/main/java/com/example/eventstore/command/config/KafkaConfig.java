package com.example.eventstore.command.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Profile("camel-kafka")
@Configuration
public class KafkaConfig {
  public static final String NOTIFICATION_TOPIC = "board-event-notifications";
  public static final String AGGREGATION_SNAPSHOT_VIEW = "board-events-aggregation";
  public static final String SNAPSHOT_TOPIC = "board-events-aggregation-topic";

  @Bean
  public NewTopic notificationTopic() {
    return TopicBuilder
        .name(NOTIFICATION_TOPIC)
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic snapshotTopic() {
    return TopicBuilder
        .name(SNAPSHOT_TOPIC)
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic aggregationTopic() {
    return TopicBuilder
        .name(AGGREGATION_SNAPSHOT_VIEW)
        .partitions(1)
        .replicas(1)
        .build();
  }

}
