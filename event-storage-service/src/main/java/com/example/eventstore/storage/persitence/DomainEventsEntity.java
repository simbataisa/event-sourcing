package com.example.eventstore.storage.persitence;

import com.example.eventstore.event.DomainEvent;
import com.example.eventstore.event.DomainEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity(name = "domainEvents")
@Table(name = "domain_events")
@Data
@EqualsAndHashCode(exclude = {"domainEvents"})
@Slf4j
public class DomainEventsEntity {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Id
  private String boardUuid;

  @OneToMany(cascade = ALL)
  @JoinColumn(name = "board_uuid")
  @OrderBy("occurredOn ASC")
  private Set<DomainEventEntity> domainEvents;

  public DomainEventsEntity() {
    this.domainEvents = new LinkedHashSet<>();
  }

  public DomainEventsEntity(final String boardUuid) {
    this.domainEvents = new LinkedHashSet<>();
    this.boardUuid = boardUuid;
  }

  public DomainEvents toModel() {
    DomainEvents model = new DomainEvents();
    model.setBoardUuid(UUID.fromString(boardUuid));
    model.setDomainEvents(domainEvents.stream().map(this.stringToDomainEventFunction()).collect(toList()));
    return model;
  }

  Function<DomainEventEntity, DomainEvent> stringToDomainEventFunction() {
    return domainEventEntity -> {
      try {
        return objectMapper.readValue(domainEventEntity.getData(), DomainEvent.class);
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
      }
      return null;
    };
  }
}
