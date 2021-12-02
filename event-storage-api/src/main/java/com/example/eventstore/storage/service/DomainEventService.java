package com.example.eventstore.storage.service;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.DomainEvents;

import java.util.List;

public interface DomainEventService {

  DomainEvents getDomainEvents(final String boardUuid);

  List<DomainEvents> getAllDomainEvents();

  void processDomainEvent(final DomainEvent event);

}
