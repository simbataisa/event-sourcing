package com.example.eventstore.model;

import com.example.eventstore.event.BoardInitialized;
import com.example.eventstore.event.BoardRenamed;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.StoryAdded;
import com.example.eventstore.event.StoryDeleted;
import com.example.eventstore.event.StoryUpdated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.vavr.API;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.Stream.ofAll;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Board {

  private UUID boardUuid;

  private String name = "New Board";

  private final Map<UUID, Story> stories = new HashMap<>();

  private final List<DomainEvent> changes = new ArrayList<>();

  public Board() {
  }

  public Board(final UUID boardUuid) {
    boardInitialized(new BoardInitialized(boardUuid, Instant.now()));
  }

  private Board boardInitialized(final BoardInitialized event) {
    log.info("boardInitialized : event=" + event);
    flushChanges();
    this.boardUuid = event.getBoardUuid();
    this.changes.add(event);
    return this;
  }

  public void renameBoard(final String name) {
    boardRenamed(new BoardRenamed(name, this.boardUuid, Instant.now()));
  }

  private Board boardRenamed(final BoardRenamed event) {
    log.info("boardRenamed : event=" + event);
    this.name = event.getName();
    this.changes.add(event);
    return this;
  }

  public void addStory(
      final UUID storyUuid,
      final String name
  ) {
    storyAdded(new StoryAdded(storyUuid, name, this.boardUuid, Instant.now()));
  }

  private Board storyAdded(final StoryAdded event) {
    log.info("storyAdded : event=" + event);
    this.stories.put(event.getStoryUuid(), event.getStory());
    this.changes.add(event);
    return this;
  }

  public void updateStory(
      final UUID storyUuid,
      final String name
  ) {
    storyUpdated(new StoryUpdated(storyUuid, name, this.boardUuid, Instant.now()));
  }

  private Board storyUpdated(final StoryUpdated event) {
    log.info("storyUpdated : event=" + event);
    this.stories.replace(event.getStoryUuid(), event.getStory());
    this.changes.add(event);
    return this;
  }

  public void deleteStory(final UUID storyUuid) {
    storyDeleted(new StoryDeleted(storyUuid, this.boardUuid, Instant.now()));
  }

  private Board storyDeleted(final StoryDeleted event) {
    log.info("storyDeleted : event=" + event);
    this.stories.remove(event.getStoryUuid());
    this.changes.add(event);
    return this;
  }

  public Map<UUID, Story> getStories() {
    return ImmutableMap.copyOf(this.stories);
  }

  public List<DomainEvent> changes() {
    return ImmutableList.copyOf(this.changes);
  }

  public void flushChanges() {
    this.changes.clear();
  }

  public void flushStories() {
    this.stories.clear();
  }

  // Builder Methods
  public static Board createFrom(
      final UUID boardUuid,
      final Collection<DomainEvent> domainEvents
  ) {
    return ofAll(domainEvents).foldLeft(new Board(boardUuid), Board::handleEvent);
  }

  public Board handleEvent(final DomainEvent domainEvent) {
    return API
        .Match(domainEvent)
        .of(
            Case($(instanceOf(BoardInitialized.class)), this::boardInitialized),
            Case($(instanceOf(BoardRenamed.class)), this::boardRenamed),
            Case($(instanceOf(StoryAdded.class)), this::storyAdded),
            Case($(instanceOf(StoryUpdated.class)), this::storyUpdated),
            Case($(instanceOf(StoryDeleted.class)), this::storyDeleted),
            Case($(), this)
        );
  }

  public static void main(String[] args) throws JsonProcessingException {
    Board board = new Board(UUID.randomUUID());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    var event = board.getChanges().get(0);
    System.out.println(event);
    System.out.println(objectMapper.writeValueAsString(event));
  }
}
