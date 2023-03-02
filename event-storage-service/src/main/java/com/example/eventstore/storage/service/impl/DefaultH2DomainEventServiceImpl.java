package com.example.eventstore.storage.service.impl;


import com.example.eventstore.Constants;
import com.example.eventstore.event.BoardInitialized;
import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.DomainEvents;
import com.example.eventstore.storage.model.DomainEventEntity;
import com.example.eventstore.storage.model.DomainEventsEntity;
import com.example.eventstore.storage.repository.DomainEventsRepository;
import com.example.eventstore.storage.service.DomainEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Profile({"default", "h2"})
@Transactional(readOnly = true)
@Service
public class DefaultH2DomainEventServiceImpl extends AbstractDomainEventService {

  private final DomainEventsRepository domainEventsRepository;
  private final ObjectMapper objectMapper;

  public DefaultH2DomainEventServiceImpl(
      ApplicationEventPublisher applicationEventPublisher,
      DomainEventsRepository domainEventsRepository,
      ObjectMapper objectMapper
  ) {
    super(applicationEventPublisher);
    this.domainEventsRepository = domainEventsRepository;
    this.objectMapper = objectMapper;
  }

  public DomainEvents getDomainEvents(final String boardUuid) {
    log.info("processDomainEvent : enter");
    log.info("processDomainEvent : boardUuid=" + boardUuid);
    return domainEventsRepository.findById(boardUuid).map(DomainEventsEntity::toModel).orElse(new DomainEventsEntity(
        boardUuid).toModel());
  }


  public List<DomainEvents> getAllDomainEvents() {
    final List<DomainEvents> domainEventsList = new ArrayList<>();
    domainEventsRepository.findAll().forEach(i -> domainEventsList.add(i.toModel()));
    return domainEventsList;
  }

  @Override
  protected void handleDomainEvent(DomainEvent event) {
    if (event instanceof BoardInitialized) {
      processBoardInitialized(event);
    } else {
      processBoardEvent(event);
    }
  }

  private void processBoardInitialized(final DomainEvent event) {
    log.info("processBoardInitialized : enter ");
    final String boardUuid = event.getBoardUuid().toString();
    DomainEventsEntity domainEventsEntity = new DomainEventsEntity(boardUuid);
    populateEntityFromEventAndEntityCopy(event, domainEventsEntity);
  }

  private void processBoardEvent(final DomainEvent event) {
    log.info("processBoardEvent : enter ");

    String boardUuid = event.getBoardUuid().toString();

    this.domainEventsRepository.findById(boardUuid)
                               .ifPresent(entity -> {
                                 log.info(
                                     "processBoardEvent : a DomainEventsEntity[{}] was entity for boardUuid[{}]. ",
                                     entity,
                                     boardUuid
                                 );
                                 populateEntityFromEventAndEntityCopy(event, entity);
                               });
  }

  private void populateEntityFromEventAndEntityCopy(
      DomainEvent event,
      DomainEventsEntity entity
  ) {
    var domainEventEntity = new DomainEventEntity();
    domainEventEntity.setId(UUID.randomUUID().toString());

    Instant occurredOn = event.occurredOn();
    domainEventEntity.setOccurredOn(LocalDateTime.ofInstant(occurredOn, ZoneOffset.UTC));
    domainEventEntity.setData(this.domainEventToJsonString(event));
    entity.getDomainEvents().add(domainEventEntity);
    this.domainEventsRepository.save(entity);
  }

  private String domainEventToJsonString(DomainEvent event) {
    try {
      return this.objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
    return Constants.EMPTY;
  }

}
