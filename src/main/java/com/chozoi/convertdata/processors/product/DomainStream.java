package com.chozoi.convertdata.processors.product;

import chozoi.products.domain_event.Key;
import chozoi.products.domain_event.Value;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Configuration
public class DomainStream {
  private static final List<String> PRODUCT_STATE_EVENTS =
      Arrays.asList("ProductCreated");

  @Bean
  public KStream<Key, Value> domainEventStream(StreamsBuilder streamsBuilder) {
    return streamsBuilder.stream(TopicConfig.PRODUCT_DOMAIN_EVENT_TOPIC);
  }

  @Bean
  public KStream<Key, Value> productStateEventStream(
      @Qualifier("domainEventStream") KStream<Key, Value> eventStream) {
    return eventStream.filter((key, value) -> PRODUCT_STATE_EVENTS.contains(value.getType())).peek((key, value) -> log.info(value.toString()));
  }
}
