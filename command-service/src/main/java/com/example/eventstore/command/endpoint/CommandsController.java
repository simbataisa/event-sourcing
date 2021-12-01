package com.example.eventstore.command.endpoint;

import com.example.eventstore.command.service.BoardService;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/my-event-store-command")
public class CommandsController {

  private final BoardService<Board, DomainEvent> service;

  @PostMapping("/boards")
  public ResponseEntity createBoard(final UriComponentsBuilder uriComponentsBuilder) {
    log.debug("createBoard : enter");
    final UUID boardUuid = this.service.createBoard();
    log.info("boardUuid {}", boardUuid);
    return ResponseEntity
        .created(uriComponentsBuilder
                     .path("/boards/{boardUuid}")
                     .buildAndExpand(boardUuid)
                     .toUri())
        .build();
  }

  @GetMapping("/boards/{boardUuid}")
  public ResponseEntity<Board> getBoard(@PathVariable("boardUuid") UUID boardUuid) {
    log.debug("getBoard : enter");
    log.info("boardUuid {}", boardUuid);
    final Board board = this.service.getBoard(boardUuid);
    return ResponseEntity.of(Optional.of(board));
  }

  @GetMapping("/boards/{boardUuid}/events")
  public ResponseEntity<List<DomainEvent>> getEvents(@PathVariable("boardUuid") UUID boardUuid) {
    return ResponseEntity.of(Optional.of(this.service.getBoardEvents(boardUuid)));
  }

  @PatchMapping("/boards/{boardUuid}")
  public ResponseEntity renameBoard(
      @PathVariable("boardUuid") UUID boardUuid,
      @RequestParam("name") String name
  ) {
    log.debug("renameBoard : enter");
    log.info("boardUuid {}", boardUuid);
    this.service.renameBoard(boardUuid, name);

    return ResponseEntity
        .accepted()
        .build();
  }

  @PostMapping("/boards/{boardUuid}/stories")
  public ResponseEntity addStoryToBoard(
      @PathVariable("boardUuid") UUID boardUuid,
      @RequestParam("name") String name,
      final UriComponentsBuilder uriComponentsBuilder
  ) {
    log.debug("addStoryToBoard : enter");

    UUID storyUuid = this.service.addStory(boardUuid, name);

    return ResponseEntity
        .created(uriComponentsBuilder
                     .path("/boards/{boardUuid}/stories/{storyUuid}")
                     .buildAndExpand(boardUuid, storyUuid)
                     .toUri())
        .build();
  }

  @PutMapping("/boards/{boardUuid}/stories/{storyUuid}")
  public ResponseEntity updateStoryOnBoard(
      @PathVariable("boardUuid") UUID boardUuid,
      @PathVariable("storyUuid") UUID storyUuid,
      @RequestParam("name") String name,
      final UriComponentsBuilder uriComponentsBuilder
  ) {
    log.debug("updateStoryOnBoard : enter");

    this.service.updateStory(boardUuid, storyUuid, name);

    return ResponseEntity
        .accepted()
        .build();
  }

  @DeleteMapping("/boards/{boardUuid}/stories/{storyUuid}")
  public ResponseEntity removeStoryFromBoard(
      @PathVariable("boardUuid") UUID boardUuid,
      @PathVariable("storyUuid") UUID storyUuid
  ) {
    log.debug("removeStoryFromBoard : enter");

    this.service.deleteStory(boardUuid, storyUuid);

    return ResponseEntity
        .accepted()
        .build();
  }

}
