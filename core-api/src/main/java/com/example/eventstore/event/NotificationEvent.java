package com.example.eventstore.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class NotificationEvent<T> extends ApplicationEvent {
  private T payload;

  public NotificationEvent(Object source, T payload) {
    super(source);
    this.payload = payload;
  }

  public NotificationEvent(Object source, Clock clock, T payload) {
    super(source, clock);
    this.payload = payload;
  }
}
