package com.chozoi.convertdata.utils;

import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
public class EventContentUtils {

  public static <T> Optional<T> toEventContent(String content, Class<T> cls) {
    T eventContent = null;
    try {
      eventContent = JsonUtils.readObject(content, cls);
    } catch (Exception e) {
      log.warn("Can't convert event content to object: " + content);
      log.warn(e);
    }

    return Optional.ofNullable(eventContent);
  }
}
