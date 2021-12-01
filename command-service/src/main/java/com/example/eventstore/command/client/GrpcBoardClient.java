package com.example.eventstore.command.client;


import com.example.eventstore.client.BoardClient;
import com.example.eventstore.command.grpc.Board;
import com.example.eventstore.command.grpc.DomainEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class GrpcBoardClient implements BoardClient<Board, DomainEvent> {

  @Override
  public void save(final Board board) {
    // TODO: to implemented
  }

  @Override
  public Board find(final UUID boardUuid) {
    // TODO: to implemented
    return null;
  }

  @Override
  public List<DomainEvent> getEvents(UUID boardUuid) {
    return null;  // TODO impl
  }

}
