package com.example.eventstore.query.endpoint;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.example.eventstore.query.dto.BoardModelDto;
import com.example.eventstore.query.service.BoardQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/my-event-store-query")
public class QueryController {

  private final BoardQueryService<Board, DomainEvent> service;

  public QueryController(final BoardQueryService service) {
    this.service = service;
  }

  @GetMapping("/boards/{boardUuid}")
  public ResponseEntity<BoardModelDto> board(@PathVariable("boardUuid") UUID boardUuid) {
    log.info("board : enter");
    Board board = this.service.find(boardUuid);
    log.info("board : board=" + board.toString());
    return ResponseEntity
        .ok(BoardModelDto.fromBoard(board));
  }

  @GetMapping("/boards/{boardUuid}/events")
  public ResponseEntity<List<DomainEvent>> getEvents(@PathVariable("boardUuid") UUID boardUuid) {
    return ResponseEntity.of(Optional.of(this.service.getBoardEvents(boardUuid)));
  }
}
