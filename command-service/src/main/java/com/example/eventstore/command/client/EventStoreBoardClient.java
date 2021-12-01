package com.example.eventstore.command.client;


import com.example.eventstore.client.BoardClient;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.DomainEvents;
import com.example.eventstore.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Profile(value = {"default", "event-store"})
@Component
public class EventStoreBoardClient implements BoardClient<Board, DomainEvent> {

  private static final String API_GW_SERVICE_NAME = "my-api-gateway";
  private static final String EVENT_STORE_SERVICE_BASE_PATH = "/my-event-store/boards";
  
  private final EventStoreFeignClient eventStoreFeignClient;

  @Override
  public void save(final Board board) {
    log.info("save : enter");

    List<DomainEvent> newChanges = board.changes();

    newChanges.forEach(domainEvent -> {
      log.info("save : domainEvent=" + domainEvent);

      ResponseEntity accepted = this.eventStoreFeignClient.addNewDomainEvent(domainEvent);
      if (!accepted.getStatusCode().equals(HttpStatus.ACCEPTED)) {
        throw new IllegalStateException("could not add DomainEvent to the Event Store");
      }
    });
    board.flushChanges();

    log.info("save : exit");
  }

  @Override
  public Board find(final UUID boardUuid) {
    log.info("find : enter");

    DomainEvents domainEvents = this.eventStoreFeignClient.getDomainEventsForBoardUuid(boardUuid);
    if (domainEvents.getDomainEvents().isEmpty()) {

      log.warn("find : exit, target[" + boardUuid.toString() + "] not found");
      throw new IllegalArgumentException("board[" + boardUuid.toString() + "] not found");
    }

    Board board = Board.createFrom(boardUuid, domainEvents.getDomainEvents());
    board.flushChanges();

    log.info("find : exit");
    return board;
  }

  @Override
  public List<DomainEvent> getEvents(UUID boardUuid) {
    DomainEvents domainEvents = this.eventStoreFeignClient.getDomainEventsForBoardUuid(boardUuid);
    return domainEvents.getDomainEvents();
  }

  @FeignClient(value = API_GW_SERVICE_NAME /*, fallback = HystrixFallbackEventStoreClient.class */)
  @LoadBalancerClient(name = "my-event-store")
  public interface EventStoreFeignClient {

    @PostMapping(path = EVENT_STORE_SERVICE_BASE_PATH + "/")
    ResponseEntity addNewDomainEvent(@RequestBody DomainEvent event);

    @GetMapping(path = EVENT_STORE_SERVICE_BASE_PATH + "/{boardUuid}")
    DomainEvents getDomainEventsForBoardUuid(@PathVariable("boardUuid") UUID boardId);
  }
}
