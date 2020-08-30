package com.example.eventstorecommand.domain.client.eventStore.config;

import com.example.eventstorecommand.domain.client.BoardClient;
import com.example.eventstorecommand.domain.client.eventStore.service.EventStoreBoardClient;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Profile("event-store-test")
@Configuration
public class EventStoreClientConfigTest {

  @Bean
  @Primary
  public BoardClient boardClient(
      @Qualifier("com.example.eventstorecommand.domain.client.eventStore.config.RestConfig$EventStoreClient") final RestConfig.EventStoreClient eventStoreClient
  ) {
    return new EventStoreBoardClient(eventStoreClient);
  }

  @Bean
  public ServletWebServerFactory servletWebServerFactory() {
    return new TomcatServletWebServerFactory();
  }

  @Bean
  public ServerList<Server> ribbonServerList(Environment env) {
    return new StaticServerList<>(new Server("localhost", Integer.parseInt(Objects.requireNonNull(env.getProperty("wiremock.server.port")))));
  }

}
