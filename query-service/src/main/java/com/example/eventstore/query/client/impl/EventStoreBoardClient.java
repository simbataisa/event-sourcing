package com.example.eventstore.query.client.impl;

import com.example.eventstore.event.DomainEvents;
import com.example.eventstore.model.Board;
import com.example.eventstore.query.client.BoardClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Slf4j
@Profile("event-store")
@Component
public class EventStoreBoardClient implements BoardClient {

  private static final String API_GW_SERVICE_NAME = "my-api-gateway";
  private static final String EVENT_STORE_SERVICE_BASE_PATH = "/my-event-store/boards";

  private final EventStoreFeignClient eventStoreFeignClient;

  public EventStoreBoardClient(EventStoreFeignClient eventStoreFeignClient) {
    this.eventStoreFeignClient = eventStoreFeignClient;
  }

  public Board find(final UUID boardUuid) {

    log.info("find : enter");
    final DomainEvents domainEvents = this.eventStoreFeignClient.getDomainEventsForBoardUuid(boardUuid);
    log.info(domainEvents.toString());
    if (null == domainEvents.getDomainEvents() || domainEvents
        .getDomainEvents()
        .isEmpty()) {
      log.warn("No event found for target[" + boardUuid.toString() + "]");
    }
    Board board = Board.createFrom(boardUuid, domainEvents.getDomainEvents());
    log.info("find : exit");
    return board;
  }

  @Override
  @CacheEvict(value = "boards", key = "#boardUuid")
  public void removeFromCache(final UUID boardUuid) {

    log.info("removeFromCache : enter");
    // this method is intentionally left blank
    log.info("removeFromCache : exit");
  }


  @FeignClient(value = API_GW_SERVICE_NAME, fallback = FallbackEventStoreFeignClient.class)
  @LoadBalancerClient(name = "my-event-store")
  public interface EventStoreFeignClient {

    @GetMapping(path = EVENT_STORE_SERVICE_BASE_PATH + "/{boardUuid}")
    @Cacheable("boards")
    DomainEvents getDomainEventsForBoardUuid(@PathVariable("boardUuid") UUID boardId);
  }

  @Component
  static class FallbackEventStoreFeignClient implements EventStoreFeignClient {
    @Override
    public DomainEvents getDomainEventsForBoardUuid(final UUID boardUuid) {
      return new DomainEvents();
    }
  }
}
