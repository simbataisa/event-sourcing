package com.example.eventstore.storage.service.impl;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.NotificationEvent;
import com.example.eventstore.storage.service.DomainEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractDomainEventService implements DomainEventService {

  protected final ApplicationEventPublisher applicationEventPublisher;

  public AbstractDomainEventService(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Transactional
  @Override
  public void processDomainEvent(DomainEvent event) {
    log.info("processDomainEvent : enter");
    log.info("processDomainEvent : event[{}] ", event);
    handleDomainEvent(event);
    broadcastNotificationToSubscribers(event);
    log.info("processDomainEvent : exit");
  }

  protected abstract void handleDomainEvent(DomainEvent event);

  protected void broadcastNotificationToSubscribers(DomainEvent domainEvent) {
    var event = new NotificationEvent<>(this.getClass().getName(), domainEvent);
    applicationEventPublisher.publishEvent(event);
  }
}
