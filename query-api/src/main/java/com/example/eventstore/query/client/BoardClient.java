package com.example.eventstore.query.client;

import com.example.eventstore.model.Board;

import java.util.List;
import java.util.UUID;

public interface BoardClient<T, E> {

    T find(final UUID boardUuid);

    List<E> getEvents(final UUID boardUuid);

}
