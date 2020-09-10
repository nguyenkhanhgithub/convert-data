package com.chozoi.convertdata.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JsonUtils {

  private static ObjectMapper objectMapper = new ObjectMapper();

  public static ObjectMapper getMappter() {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }

  //  static {
  //    objectMapper.getRegisteredModuleIds();
  //  }

  public static <T> List<T> readArray(String jsonValue) throws IOException {

    return Objects.isNull(jsonValue)
        ? null
        : getMappter().readValue(jsonValue, new TypeReference<List<T>>() {});
  }

  public static <T> T readObject(String jsonValue, Class<T> cls) throws IOException {
    return Objects.isNull(jsonValue) ? null : getMappter().readValue(jsonValue, cls);
  }
}
