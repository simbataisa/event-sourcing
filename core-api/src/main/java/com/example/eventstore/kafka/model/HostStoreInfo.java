package com.example.eventstore.kafka.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class HostStoreInfo {

  private final String host;
  private final int port;
  private final List<String> standByHost;


}
