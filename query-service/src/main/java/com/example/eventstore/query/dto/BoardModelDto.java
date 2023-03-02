package com.example.eventstore.query.dto;

import com.example.eventstore.model.Board;
import com.example.eventstore.model.Story;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Data
@Builder
@Slf4j
public class BoardModelDto {

  private String name;
  private Collection<Story> backlog;

  public static BoardModelDto fromBoard(final Board board) {
    return BoardModelDto.builder().name(board.getName()).backlog(board.getStories().values()).build();
  }
}
