package com.example.eventstore.storage.endpoint;


import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.DomainEvents;
import com.example.eventstore.storage.service.DomainEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/my-event-store/boards")
public class StorageController {

  private final DomainEventService service;

  @PostMapping("/")
  public ResponseEntity saveEvent(@RequestBody DomainEvent domainEvent) {
    this.service.processDomainEvent(domainEvent);
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/{boardUuid}")
  public ResponseEntity<DomainEvents> domainEvents(@PathVariable("boardUuid") UUID boardUuid) {
    log.info("Get all events of {}", boardUuid);
    return ResponseEntity.ok(this.service.getDomainEvents(boardUuid.toString()));
  }

  @GetMapping("/")
  public ResponseEntity<List<DomainEvents>> domainAllEvents() {
    log.info("Get all events");
    return ResponseEntity.ok(this.service.getAllDomainEvents());
  }

}
