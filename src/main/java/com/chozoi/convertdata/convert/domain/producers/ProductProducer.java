package com.chozoi.convertdata.convert.domain.producers;

import chozoi.products.domain_event.Key;
import chozoi.products.domain_event.Value;
import com.chozoi.convertdata.convert.domain.entities.DomainEvent;
import com.chozoi.convertdata.processors.product.TopicConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ProductProducer {

  @Autowired
  private KafkaTemplate<Key, Value> productKafkaTemplate;

  public void save(DomainEvent event) {
    try {
        this.productKafkaTemplate.send(TopicConfig.PRODUCT_DOMAIN_EVENT_TOPIC, event.toValueAvro());
    } catch (Exception e) {
      log.debug(e);
    }
  }
}
