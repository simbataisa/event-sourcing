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

import static com.example.eventstore.query.config.CommonConfig.BOARD_CACHE_NAME;
import static com.example.eventstore.query.config.CommonConfig.BOARD_EVENTS_CACHE_NAME;

@Slf4j
@RequiredArgsConstructor
@Profile(value = {"default", "event-store"})
@Service
public class BoardQueryServiceImpl implements BoardQueryService<Board, DomainEvent> {

  private final BoardClient<Board, DomainEvent> client;

  @Cacheable(value = BOARD_CACHE_NAME)
  public Board find(final UUID boardUuid) {
    log.info("find : enter");
    Board board = this.client.find(boardUuid);
    log.info("find : board=" + board);
    log.info("find : exit");
    return board;
  }

  @Cacheable(value = BOARD_EVENTS_CACHE_NAME)
  @Override
  public List<DomainEvent> getBoardEvents(UUID boardUuid) {
    return this.client.getEvents(boardUuid);
  }

  @CacheEvict(value = {BOARD_CACHE_NAME, BOARD_EVENTS_CACHE_NAME})
  @Override
  public void uncacheTarget(final UUID boardUuid) {
    log.info("uncacheTarget {}", boardUuid);
  }
}
