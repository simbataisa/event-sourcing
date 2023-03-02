package com.example.eventstore.storage.repository;


import com.example.eventstore.common.util.CommonUtils;
import com.example.eventstore.repository.KVRepository;
import com.example.eventstore.storage.model.DomainEventsEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Slf4j
@Profile("rocksdb")
@Repository
public class DomainEventRocksDBRepository implements KVRepository<String, DomainEventsEntity> {

  private static final String FILE_NAME = "event-store-rocksdb";
  private static final String USER_HOME = CommonUtils.getUsersHomeDir();
  private static final String FILE_DIR = USER_HOME + "/tmp/rocks";

  private final ObjectMapper objectMapper;
  private RocksDB rocksDB;

  public DomainEventRocksDBRepository(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    RocksDB.loadLibrary();
    try (final Options options = new Options()) {
      options.setCreateIfMissing(true);
      log.info("Initializing RocksDB...");
      log.info("USER_HOME {}", USER_HOME);
      log.info("FILE_DIR {}", FILE_DIR);
      log.info("FILE_NAME {}", FILE_NAME);
      File baseDir = new File(FILE_DIR, FILE_NAME);
      log.info("baseDir.getParentFile().toPath() = " + baseDir.getParentFile().toPath());
      log.info("baseDir.getAbsoluteFile().toPath() = " + baseDir.getAbsoluteFile().toPath());
      Files.createDirectories(baseDir.getParentFile().toPath());
      Files.createDirectories(baseDir.getAbsoluteFile().toPath());
      this.rocksDB = RocksDB.open(options, baseDir.getAbsolutePath());
      log.info("RocksDB initialized");
    } catch (RocksDBException | IOException e) {
      log.error("Error initializing RocksDB. Exception: '{}', message: '{}'", e.getCause(), e.getMessage(), e);
    }
  }

  @Override
  public synchronized boolean save(
      String key,
      DomainEventsEntity value
  ) {
    log.info("saving value '{}' with key '{}'", value, key);
    try {
      this.rocksDB.put(key.getBytes(), objectMapper.writeValueAsBytes(value));
    } catch (RocksDBException | JsonProcessingException e) {
      log.error("Error saving entry. Cause: '{}', message: '{}'", e.getCause(), e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public synchronized boolean save(DomainEventsEntity value) {
    return this.save(value.getBoardUuid(), value);
  }

  @Override
  public synchronized Optional<DomainEventsEntity> find(String key) {
    DomainEventsEntity value = null;

    try {
      byte[] bytes = this.rocksDB.get(key.getBytes());
      if (bytes != null) {
        value = this.objectMapper.readValue(bytes, DomainEventsEntity.class);
      }
    } catch (RocksDBException | IOException e) {
      log.error(
          "Error retrieving the entry with key: {}, cause: {}, message: {}",
          key,
          e.getCause(),
          e.getMessage(),
          e
      );
    }

    log.info("finding key '{}' returns '{}'", key, value);
    return value != null ? Optional.of(value) : Optional.empty();
  }

  @Override
  public Iterable<DomainEventsEntity> findAll() {
    return null;  // TODO impl
  }

  @Override
  public synchronized boolean delete(String key) {
    log.info("deleting key '{}'", key);
    try {
      this.rocksDB.delete(key.getBytes());
    } catch (RocksDBException e) {
      log.error("Error deleting entry, cause: '{}', message: '{}'", e.getCause(), e.getMessage());
      return false;
    }
    return true;
  }
}
