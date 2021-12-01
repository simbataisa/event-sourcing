package com.example.eventstore.command.service;

import java.util.UUID;

public interface BoardService<T> {

  UUID createBoard();

  T getBoard(final UUID boardUuid);

  T renameBoard(final UUID boardUuid, final String name);

  UUID addStory(final UUID boardUuid, final String name);

  void updateStory(final UUID boardUuid, final UUID storyUuid, final String name);

  void deleteStory(final UUID boardUuid, final UUID storyUuid);

}
