package com.example.eventstore.command.service.impl;

import com.example.eventstore.client.BoardClient;
import com.example.eventstore.command.service.BoardService;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Profile(value = {"default", "event-store", "kafka", "camel-kafka"})
@Service
public class BoardServiceImpl implements BoardService<Board, DomainEvent> {

  private final BoardClient<Board, DomainEvent> client;

  public UUID createBoard() {
    log.debug("createBoard : enter");

    Board board = new Board(UUID.randomUUID());
    this.client.save(board);

    return board.getBoardUuid();
  }

  public Board getBoard(final UUID boardUuid) {
    log.debug("getBoard : enter");
    return this.client.find(boardUuid);
  }

  @Override
  public List<DomainEvent> getBoardEvents(UUID boardUuid) {
    return this.client.getEvents(boardUuid);
  }

  public Board renameBoard(final UUID boardUuid, final String name) {
    log.debug("renameBoard : enter");

    Board board = this.client.find(boardUuid);
    board.renameBoard(name);
    this.client.save(board);
    return board;
  }

  public UUID addStory(final UUID boardUuid, final String name) {
    log.debug("addStory : enter");

    Board board = this.client.find(boardUuid);
    UUID storyUuid = UUID.randomUUID();
    board.addStory(storyUuid, name);

    this.client.save(board);
    return storyUuid;
  }

  public void updateStory(final UUID boardUuid, final UUID storyUuid, final String name) {
    log.debug("updateStory : enter");

    Board board = this.client.find(boardUuid);
    board.updateStory(storyUuid, name);

    this.client.save(board);
  }

  public void deleteStory(final UUID boardUuid, final UUID storyUuid) {
    log.debug("deleteStory : enter");

    Board board = this.client.find(boardUuid);
    board.deleteStory(storyUuid);

    this.client.save(board);
  }
}
