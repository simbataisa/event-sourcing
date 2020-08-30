package com.example.eventstorecommand.domain.client.eventStore.config;

import com.example.eventstorecommand.domain.client.BoardClient;
import com.example.eventstorecommand.domain.client.eventStore.service.EventStoreBoardClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("event-store")
@Configuration
public class EventStoreClientConfig {

  @Bean
  @Primary
  public BoardClient boardClient(
      @Qualifier("com.example.eventstorecommand.domain.client.eventStore.config.RestConfig$EventStoreClient") final RestConfig.EventStoreClient eventStoreClient
  ) {
    return new EventStoreBoardClient(eventStoreClient);
  }
}
