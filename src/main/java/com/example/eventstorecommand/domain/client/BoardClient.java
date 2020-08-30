package com.example.eventstorecommand.domain.client;

import com.example.eventstorecommand.domain.model.Board;

import java.util.UUID;

public interface BoardClient {

  void save(final Board board);

  Board find(final UUID boardUuid);

}
