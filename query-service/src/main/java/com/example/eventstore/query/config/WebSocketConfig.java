package com.example.eventstore.query.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Controller
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private static final String WS_TOPIC_BROKER = "/topic";
  private static final String WS_APP_PREFIX = "/app";
  private static final String WS_NOTIFICATION_EP = "/notification";
  public static final String WS_BOARD_EVENT_TOPIC = WS_TOPIC_BROKER + "/board-events";
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(WS_TOPIC_BROKER);
    config.setApplicationDestinationPrefixes(WS_APP_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(WS_NOTIFICATION_EP).setAllowedOrigins("*").withSockJS();
    registry.addEndpoint(WS_NOTIFICATION_EP).setAllowedOrigins("*");
  }
}
