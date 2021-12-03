package com.example.eventstore.service.impl.kafka;

import com.example.eventstore.exception.NotFoundException;
import com.example.eventstore.kafka.model.HostStoreInfo;
import com.example.eventstore.service.kafka.KStreamMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsMetadata;
import org.apache.kafka.streams.state.HostInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class KStreamMetadataServiceImpl implements KStreamMetadataService {

  private final KafkaStreams streams;

  @Override
  public List<HostStoreInfo> streamsMetadata() {
    val metadata = this.streams.metadataForAllStreamsClients();
    return mapInstancesToHostStoreInfo(metadata);
  }

  @Override
  public List<HostStoreInfo> streamsMetadataForStore(String store) {
    val metadata = streams.streamsMetadataForStore(store);
    return mapInstancesToHostStoreInfo(metadata);
  }

  public <T> HostStoreInfo streamsMetadataForStoreAndKey(
      String store,
      T key,
      Serializer<T> serializer
  ) {
    val metadata = streams.queryMetadataForKey(store, key, serializer);
    if (metadata == null) {
      throw new NotFoundException(String.format(
          "No metadata could be found for store : ${store}, and key type : %s",
          key.getClass().getName()
      ));
    }


    return new HostStoreInfo(
        metadata.activeHost().host(),
        metadata.activeHost().port(),
        metadata.standbyHosts().stream().map(HostInfo::host).collect(Collectors.toList())
    );
  }

  private List<HostStoreInfo> mapInstancesToHostStoreInfo(Collection<StreamsMetadata> streamsMetadata) {
    return streamsMetadata.stream().map(metadata ->
                                            new HostStoreInfo(
                                                metadata.host(),
                                                metadata.port(),
                                                new ArrayList<>(metadata.stateStoreNames())
                                            ))
                          .collect(Collectors.toList());
  }
}
