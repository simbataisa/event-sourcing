package com.example.eventstore.storage.service.impl;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.NotificationEvent;
import com.example.eventstore.storage.service.EventSubscriptionManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class EventSubscriptionManagementServiceImpl implements EventSubscriptionManagementService {

  private final Set<String> subscribers = new ConcurrentSkipListSet<>();

  private final WebClient webClient = WebClient
      .builder()
      .baseUrl("http://localhost:8765/")
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();

  private final ObjectMapper objectMapper;

  public EventSubscriptionManagementServiceImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean subscribe(String client) {
    if (StringUtils.isBlank(client)) {
      throw new IllegalArgumentException("client should not be blank");
    }
    return subscribers.add(client);
  }

  @Override
  public boolean unsubscribe(String client) {
    if (StringUtils.isBlank(client)) {
      throw new IllegalArgumentException("client should not be blank");
    }
    return subscribers.remove(client);
  }

  @Override
  public void broadcastEvent(DomainEvent event) {
    log.info("broadcasting event {}", event);
    for (String client : subscribers) {
      log.info("client {}", client);
      final String path = client + "/boards/cache/" + event.getBoardUuid();
      webClient
          .post()
          .uri(path)
          .body(BodyInserters.fromValue(event))
          .exchange()
          .doOnSuccess(
              r -> log.info("Notification event was sent to client {} successfully? {}, status code {}", client,
                            r.statusCode().is2xxSuccessful(), r.statusCode().value()))
          .doOnError(e -> log.error("Got error while trying to send notification to client {}", client, e))
          .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
  }

  @Async
  @EventListener
  public void handleNotificationEvent(NotificationEvent<DomainEvent> notificationEvent) {
    log.info("Received a notification event {}", notificationEvent.getPayload().eventType());
    this.broadcastEvent(notificationEvent.getPayload());
  }

}
