package com.chozoi.convertdata.utils;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;

public class MaterializedUtils {

  public static Materialized<Long, Long, KeyValueStore<Bytes, byte[]>> long2long(String storeName) {
    return Materialized.<Long, Long, KeyValueStore<Bytes, byte[]>>as(storeName)
        .withKeySerde(Serdes.Long())
        .withValueSerde(Serdes.Long());
  }
}
