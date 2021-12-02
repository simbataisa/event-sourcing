package com.example.eventstore.query.service;

import com.example.eventstore.model.Board;

import java.util.UUID;

public interface BoardService {

  //    @Cacheable( "boards" )
  Board find(final UUID boardUuid);

  void uncacheTarget(final UUID boardUuid);
}
