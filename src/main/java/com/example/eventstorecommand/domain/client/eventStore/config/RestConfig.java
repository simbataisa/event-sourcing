package com.example.eventstorecommand.domain.client.eventStore.config;

import com.example.eventstorecommand.domain.event.DomainEvent;
import com.example.eventstorecommand.domain.event.DomainEvents;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Configuration
public class RestConfig {

  //@FeignClient(value = "esd-event-store" /*, fallback = HystrixFallbackEventStoreClient.class */)
  @FeignClient(value = "netflix-zuul-api-gateway-server" /*, fallback = HystrixFallbackEventStoreClient.class */)
  @RibbonClient(name = "esd-event-store")
  public interface EventStoreClient {

    //@PostMapping(path = "/")
    @PostMapping(path = "/esd-event-store")
    ResponseEntity addNewDomainEvent(@RequestBody DomainEvent event);

    //@GetMapping(path = "/{boardUuid}")
    @GetMapping(path = "/esd-event-store/{boardUuid}")
    DomainEvents getDomainEventsForBoardUuid(@PathVariable("boardUuid") UUID boardId);
  }
}