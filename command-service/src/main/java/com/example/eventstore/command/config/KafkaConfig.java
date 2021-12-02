package com.example.eventstore.command.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import static com.example.eventstore.Constants.AGGREGATION_SNAPSHOT_VIEW;
import static com.example.eventstore.Constants.NOTIFICATION_TOPIC;
import static com.example.eventstore.Constants.SNAPSHOT_TOPIC;

@Profile("camel-kafka")
@Configuration
public class KafkaConfig {

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
