package com.example.eventstore;

public final class Constants {

  public static final String CAMEL_DIRECT_ROUTE_PREFIX = "direct://";
  public static final String NOTIFICATION_TOPIC = "board-event-notifications";
  public static final String AGGREGATION_SNAPSHOT_VIEW = "board-events-aggregation";
  public static final String SNAPSHOT_TOPIC = "board-events-aggregation-topic";
  public static final String BOOTSTRAP_SERVER_ENV_KEY = "spring.kafka.bootstrap-servers";
  public static final String CLOUD_IP_ADDRESS_ENV_KEY = "spring.cloud.client.ip-address";
  public static final String SERVER_PORT_ENV_KEY = "server.port";

  public static final String EMPTY = "";
  public static final String TRUSTED_PACKAGES = "com.example.eventstore.event";

  private Constants(){}
}
