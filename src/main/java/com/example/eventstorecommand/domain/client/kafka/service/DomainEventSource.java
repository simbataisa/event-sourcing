package com.example.eventstorecommand.domain.client.kafka.service;


import com.example.eventstorecommand.domain.event.DomainEvent;

public interface DomainEventSource {

    void publish(final DomainEvent event);
}
