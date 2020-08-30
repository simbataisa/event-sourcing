package com.example.eventstorecommand;

import com.example.eventstorecommand.domain.event.BoardInitialized;
import com.example.eventstorecommand.domain.event.DomainEvents;
import com.example.eventstorecommand.domain.model.Board;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

public class Sample {

  public static void main(String[] args) throws JsonProcessingException {
    Board board = new Board(UUID.randomUUID());
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    System.out.println(objectMapper.writeValueAsString(board));

    DomainEvents domainEvents = new DomainEvents();
    domainEvents.setBoardUuid(board.getBoardUuid());
    domainEvents.setDomainEvents(Collections.singletonList(new BoardInitialized(board.getBoardUuid(), Instant.now())));
    System.out.println(objectMapper.writeValueAsString(domainEvents));
  }
}
