package com.example.eventstore.command.client;

import java.util.UUID;

public interface BoardClient<T> {

  void save(final T board);

  T find(final UUID boardUuid);

}
