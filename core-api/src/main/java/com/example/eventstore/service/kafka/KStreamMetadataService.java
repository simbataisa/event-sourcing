package com.example.eventstore.service.kafka;

import com.example.eventstore.kafka.model.HostStoreInfo;
import org.apache.kafka.common.serialization.Serializer;

import java.util.List;

public interface KStreamMetadataService {

  /**
   * Get the metadata for all the instances of this Kafka Streams application
   *
   * @return List of { @link HostStoreInfo}
   */
  List<HostStoreInfo> streamsMetadata();

  List<HostStoreInfo> streamsMetadataForStore(String store);

  <T> HostStoreInfo streamsMetadataForStoreAndKey(String store, T key, Serializer<T> serializer);
}
