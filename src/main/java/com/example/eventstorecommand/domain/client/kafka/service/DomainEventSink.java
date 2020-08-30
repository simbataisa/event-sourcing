package com.example.eventstorecommand.domain.client.kafka.service;

import com.example.eventstorecommand.domain.event.DomainEvent;
import org.apache.kafka.streams.kstream.KStream;

public interface DomainEventSink {

  void process(KStream<String, DomainEvent> input);

}
