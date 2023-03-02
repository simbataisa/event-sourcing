package com.example.eventstore.query.service;

import java.util.List;
import java.util.UUID;

public interface BoardQueryService<T, E> {

  //    @Cacheable( "boards" )
  T find(final UUID boardUuid);

  List<E> getBoardEvents(final UUID boardUuid);

  void uncacheTarget(final UUID boardUuid);
}
