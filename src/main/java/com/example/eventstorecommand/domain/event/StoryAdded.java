package com.example.eventstorecommand.domain.event;

import com.example.eventstorecommand.domain.model.Story;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({ "eventType", "boardUuid", "occurredOn", "storyUuid", "name" })
public class StoryAdded extends DomainEvent {

  private final UUID storyUuid;
  private final String name;

  @JsonCreator
  public StoryAdded(
      @JsonProperty("storyUuid") final UUID storyUuid,
      @JsonProperty("name") final String name,
      @JsonProperty("boardUuid") final UUID boardUuid,
      @JsonProperty("occurredOn") final Instant when
  ) {
    super(boardUuid, when);

    this.storyUuid = storyUuid;
    this.name = name;

  }

  public Story getStory() {

    Story story = new Story();
    story.setName(this.name);

    return story;
  }

  @Override
  @JsonIgnore
  public String eventType() {

    return this.getClass().getSimpleName();
  }

}
