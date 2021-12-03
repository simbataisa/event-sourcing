package com.example.eventstore.repository;

import java.util.Optional;

public interface KVRepository<K, V> {

  boolean save(
      K key,
      V value
  );

  boolean save(V value);

  Optional<V> find(K key);

  Iterable<V> findAll();

  boolean delete(K key);
}
