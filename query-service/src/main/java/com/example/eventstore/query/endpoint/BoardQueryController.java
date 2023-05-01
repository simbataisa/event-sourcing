package com.example.eventstore.query.endpoint;

import com.example.eventstore.event.BoardInitialized;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import com.example.eventstore.query.dto.BoardModelDto;
import com.example.eventstore.query.service.BoardQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/my-event-store-query/boards")
public class BoardQueryController {

  private final BoardQueryService<Board, DomainEvent> service;

  public BoardQueryController(final BoardQueryService<Board, DomainEvent> service) {
    this.service = service;
  }

  @GetMapping("/{boardUuid}")
  public ResponseEntity<BoardModelDto> board(
      @PathVariable("boardUuid")
      UUID boardUuid
  ) {
    log.info("board : enter");
    Board board = this.service.find(boardUuid);
    log.info("board : board=" + board.toString());
    return ResponseEntity
        .ok(BoardModelDto.fromBoard(board));
  }

  @GetMapping("/{boardUuid}/events")
  public ResponseEntity<List<DomainEvent>> getEvents(
      @PathVariable("boardUuid")
      UUID boardUuid
  ) {
    log.info("Getting all events for board with id {}", boardUuid);
    return ResponseEntity.of(Optional.of(this.service.getBoardEvents(boardUuid)));
  }

  @PostMapping("/cache/{boardUuid}")
  public ResponseEntity evictCache(
      @PathVariable("boardUuid")
      UUID boardUuid,
      @RequestBody
      DomainEvent domainEvent
  ) {
    try {
      if (domainEvent instanceof BoardInitialized) {
        log.info("No need to evict cache");
      } else {
        this.service.uncacheTarget(boardUuid);
      }
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
