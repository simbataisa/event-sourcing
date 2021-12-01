package com.example.eventstore.client;

import java.util.List;
import java.util.UUID;

public interface BoardClient<T, E> {

  void save(final T board);

  T find(final UUID boardUuid);

  List<E> getEvents(final UUID boardUuid);

}
