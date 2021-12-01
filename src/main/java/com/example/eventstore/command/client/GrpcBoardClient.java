package com.example.eventstore.command.client;


import com.example.eventstore.command.grpc.Board;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class GrpcBoardClient implements BoardClient<Board> {

  @Override
  public void save(final Board board) {
    // TODO: to implemented
  }

  @Override
  public Board find(final UUID boardUuid) {
    // TODO: to implemented
    return null;
  }

}
