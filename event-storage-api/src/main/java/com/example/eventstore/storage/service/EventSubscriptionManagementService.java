package com.example.eventstore.storage.service;

import com.example.eventstore.event.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventSubscriptionManagementService {

  boolean subscribe(String client);

  boolean unsubscribe(String client);

  void broadcastEvent(DomainEvent event);
}
