package com.example.eventstore;

import org.apache.camel.CamelContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

  private final CamelContext camelContext;

  public ApplicationStartup(CamelContext camelContext) {
    this.camelContext = camelContext;
  }

  /**
   * This event is executed as late as conceivably possible to indicate that
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    System.out.println("-------------------");
    System.out.println(camelContext.getEndpoints());
    System.out.println(camelContext.getRoutes());
  }
}
