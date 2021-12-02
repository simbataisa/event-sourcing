package com.example.eventstore.query.client;

import com.example.eventstore.model.Board;

import java.util.UUID;

public interface BoardClient {

    Board find(final UUID boardUuid);

    void removeFromCache(final UUID boardUuid);

}
