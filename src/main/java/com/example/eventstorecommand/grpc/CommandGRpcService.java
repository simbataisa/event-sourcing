package com.example.eventstorecommand.grpc;

import com.example.eventstorecommand.domain.service.BoardService;
import com.example.eventstorecommand.grpc.BoardCommandServiceGrpc.BoardCommandServiceImplBase;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@GRpcService
public class CommandGRpcService extends BoardCommandServiceImplBase {

  private final BoardService boardService;

  public CommandGRpcService(BoardService boardService) {
    this.boardService = boardService;
  }

  @Override
  public void createBoard(Empty request, StreamObserver<CreateBoardResponse> responseObserver) {

    UUID boardUuid = boardService.createBoard();
    final CreateBoardResponse.Builder replyBuilder = CreateBoardResponse.newBuilder().setBoardUuid(boardUuid.toString());
    responseObserver.onNext(replyBuilder.build());
    responseObserver.onCompleted();
    log.info("Returning " + boardUuid);
  }

  public void renameBoard(com.example.eventstorecommand.grpc.RenameBoardRequest request,
                          io.grpc.stub.StreamObserver<com.example.eventstorecommand.grpc.RenameBoardResponse> responseObserver) {
    com.example.eventstorecommand.domain.model.Board board = boardService
        .renameBoard(UUID.fromString(request.getBoardUuid()), request.getBoardName());
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
    Instant instant = Instant.now();
    System.out.println(instant.toEpochMilli());
    System.out.println(board);
  }
}
