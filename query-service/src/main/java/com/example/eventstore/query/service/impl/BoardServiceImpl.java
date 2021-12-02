package com.example.eventstore.query.service.impl;

import com.example.eventstore.model.Board;
import com.example.eventstore.query.client.BoardClient;
import com.example.eventstore.query.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Profile(value = {"default", "event-store"})
@Service
public class BoardServiceImpl implements BoardService {

  private final BoardClient client;

  //    @Cacheable( "boards" )
  public Board find(final UUID boardUuid) {
    log.info("find : enter");
    Board board = this.client.find(boardUuid);
    log.info("find : board=" + board);
    log.info("find : exit");
    return board;
  }

  @CacheEvict(value = "boards", key = "#boardUuid")
  public void uncacheTarget(final UUID boardUuid) {
    this.client.removeFromCache(boardUuid);
  }
}
