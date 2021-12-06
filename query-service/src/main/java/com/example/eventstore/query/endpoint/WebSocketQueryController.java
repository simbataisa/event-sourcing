package com.example.eventstore.query.endpoint;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketQueryController {

  @MessageMapping("/send-event")
  @SendTo("/topic/board-events")
  public String boardEvents(final String message) {
    log.info(message);
    return message;
  }
}
