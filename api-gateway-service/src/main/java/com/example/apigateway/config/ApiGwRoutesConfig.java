package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGwRoutesConfig {

  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
                  .route(r -> r.path("/my-event-store-query/**")
                               .uri("lb://MY-EVENT-STORE-QUERY"))
                  .route(r -> r.path("/my-event-store-command/**")
                               .uri("lb://MY-EVENT-STORE-COMMAND"))
                  .route(r -> r.path("/my-event-store/**")
                               .uri("lb://MY-EVENT-STORE"))
                  .build();
  }
}
