package com.chozoi.convertdata.utils;

import org.springframework.kafka.support.serializer.JsonSerde;

public class CustomSerdes {

  public static <T> JsonSerde<T> json(Class<T> clz) {

    return new JsonSerde<>(clz, JsonUtils.getMappter());
  }
}
