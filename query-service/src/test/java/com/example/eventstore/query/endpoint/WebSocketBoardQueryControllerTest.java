package com.example.eventstore.query.endpoint;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Scanner;

@Disabled
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"spring.profiles.active=event-store"})
class WebSocketBoardQueryControllerTest {

  private static final String WS_TOPIC = "/topic/board-events";

  public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(
        StompSession session,
        StompHeaders connectedHeaders
    ) {
      logger.info("New session established : " + session.getSessionId());
      session.subscribe(WS_TOPIC, this);
      logger.info("Subscribed to " + WS_TOPIC);
    }

    @Override
    public void handleException(
        StompSession session,
        StompCommand command,
        StompHeaders headers,
        byte[] payload,
        Throwable exception
    ) {
      logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
      return String.class;
    }

    @Override
    public void handleFrame(
        StompHeaders headers,
        Object payload
    ) {
      String msg = (String) payload;
      logger.info("Received : " + msg);
    }

    /**
     * A sample message instance.
     *
     * @return instance of <code>Message</code>
     */
    private String getSampleMessage() {
      return "hello from WebSocketBoardQueryControllerTest";
    }
  }

  @Test
  void testStream() {
    WebSocketClient client = new StandardWebSocketClient();

    WebSocketStompClient stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new StringMessageConverter());

    StompSessionHandler sessionHandler = new MyStompSessionHandler();
    stompClient.connect("ws://localhost:9081/notification", sessionHandler);

    new Scanner(System.in).nextLine(); // Don't close immediately.
  }
}
