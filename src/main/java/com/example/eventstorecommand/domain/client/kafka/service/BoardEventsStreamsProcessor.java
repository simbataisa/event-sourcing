package com.example.eventstorecommand.domain.client.kafka.service;

import com.example.eventstorecommand.domain.event.DomainEvent;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface BoardEventsStreamsProcessor {

  String INPUT = "boardEventsInput";

  @Input(INPUT)
  KStream<String, DomainEvent> boardEventsInput();
}
