package com.example.eventstore.query.dto;

import com.example.eventstore.model.Board;
import com.example.eventstore.model.Story;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Data
@Slf4j
public class BoardModelDto {

  private String name;
  private Collection<Story> backlog;

  public static BoardModelDto fromBoard(final Board board) {
    BoardModelDto model = new BoardModelDto();
    model.setName(board.getName());
    if (null != board.getStories() && !board.getStories().isEmpty()) {
      model.setBacklog(board.getStories().values());
    }
    return model;
  }

}
