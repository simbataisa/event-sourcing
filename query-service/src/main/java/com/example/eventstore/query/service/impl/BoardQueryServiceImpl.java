package com.example.eventstore.query.service.impl;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.example.eventstore.query.client.BoardClient;
import com.example.eventstore.query.service.BoardQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Profile(value = {"default", "event-store"})
@Service
public class BoardQueryServiceImpl implements BoardQueryService<Board, DomainEvent> {

  private final BoardClient<Board, DomainEvent> client;

  // This is cached at OpenFeign Client instead.
  // This can be enabled to avoid reconstructing state of object from cached events.
  //@Cacheable(value = "boards", key = "#boardUuid")
  public Board find(final UUID boardUuid) {
    log.info("find : enter");
    Board board = this.client.find(boardUuid);
    log.info("find : board=" + board);
    log.info("find : exit");
    return board;
  }

  @Override
  public List<DomainEvent> getBoardEvents(UUID boardUuid) {
    return this.client.getEvents(boardUuid);
  }

  public void uncacheTarget(final UUID boardUuid) {
    this.client.removeFromCache(boardUuid);
  }
}
