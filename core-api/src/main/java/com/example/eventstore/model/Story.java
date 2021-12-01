package com.example.eventstore.model;

import lombok.Data;

@Data
public class Story {
  private final String name;

  public Story() {
    this.name = null;
  }

  public Story(final String name) {
    this.name = name;
  }
}
