package com.example.eventstore;

public final class Constants {

  public static final String CAMEL_DIRECT_ROUTE_PREFIX = "direct://";
  public static final String NOTIFICATION_TOPIC = "board-event-notifications";
  public static final String AGGREGATION_SNAPSHOT_VIEW = "board-events-aggregation";
  public static final String SNAPSHOT_TOPIC = "board-events-aggregation-topic";
  public static final String KAFKA_BROKER = "localhost:9092";
  
  private Constants(){}
}
