package com.example.eventstore.command.service.impl;

import com.example.eventstore.command.grpc.Board;
import com.example.eventstore.command.grpc.BoardCommandServiceGrpc;
import com.example.eventstore.command.grpc.CreateBoardResponse;
import com.example.eventstore.command.grpc.DomainEvent;
import com.example.eventstore.command.grpc.RenameBoardRequest;
import com.example.eventstore.command.grpc.RenameBoardResponse;
import com.example.eventstore.command.grpc.Story;
import com.example.eventstore.command.service.BoardService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@GRpcService
public class CommandGRpcService extends BoardCommandServiceGrpc.BoardCommandServiceImplBase implements BoardService<Board, DomainEvent>{
  
  @Override
  public void createBoard(Empty request, StreamObserver<CreateBoardResponse> responseObserver) {

    UUID boardUuid = this.createBoard();
    final CreateBoardResponse.Builder replyBuilder = CreateBoardResponse.newBuilder().setBoardUuid(boardUuid.toString());
    responseObserver.onNext(replyBuilder.build());
    responseObserver.onCompleted();
    log.info("Returning " + boardUuid);
  }

  public void renameBoard(
      RenameBoardRequest request,
      io.grpc.stub.StreamObserver<RenameBoardResponse> responseObserver) {
    Board board = this.renameBoard(UUID.fromString(request.getBoardUuid()), request.getBoardName());
    final RenameBoardResponse.Builder replyBuilder = RenameBoardResponse.newBuilder();
    final Board.Builder boardBuilder = Board.newBuilder();
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.map(board, boardBuilder);
    responseObserver.onNext(replyBuilder.setBoard(boardBuilder.build()).build());
    responseObserver.onCompleted();
    log.info("Returning " + board);
  }

  public static void main(String[] args) {
    Board board = Board.newBuilder().setBoardUuid(UUID.randomUUID().toString()).setName("My Board Name")
                       .putStories(UUID.randomUUID().toString(), Story.newBuilder().setName("Story Name").build())
                       .build();
    final Instant instant = Instant.now();
    log.info(String.valueOf(instant.toEpochMilli()));
    log.info(board.toString());
  }

  @Override
  public UUID createBoard() {
    return null;  // TODO impl
  }

  @Override
  public Board getBoard(UUID boardUuid) {
    return null;  // TODO impl
  }

  @Override
  public List<DomainEvent> getBoardEvents(UUID boardUuid) {
    return null;  // TODO impl
  }

  @Override
  public Board renameBoard(
      UUID boardUuid,
      String name
  ) {
    return null;  // TODO impl
  }

  @Override
  public UUID addStory(
      UUID boardUuid,
      String name
  ) {
    return null;  // TODO impl
  }

  @Override
  public void updateStory(
      UUID boardUuid,
      UUID storyUuid,
      String name
  ) {
    // TODO impl
  }

  @Override
  public void deleteStory(
      UUID boardUuid,
      UUID storyUuid
  ) {
    // TODO impl
  }
}
