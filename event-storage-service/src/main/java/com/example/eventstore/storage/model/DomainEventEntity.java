package com.example.eventstore.storage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = "domainEvent")
@Table(name = "domain_event")
@Data
@EqualsAndHashCode(exclude = {"id", "occurredOn"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainEventEntity implements Comparable<DomainEventEntity> {

  @Id
  private String id;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime occurredOn;

  @Lob
  private String data;

  @Column(name = "board_uuid")
  private String boardUuid;

  @Override
  public int compareTo(DomainEventEntity o) {
    if (this.getOccurredOn().isAfter(o.getOccurredOn())) {
      return 1;
    } else if (this.getOccurredOn().isBefore(o.getOccurredOn())) {
      return -1;
    }
    return 0;
  }
}
