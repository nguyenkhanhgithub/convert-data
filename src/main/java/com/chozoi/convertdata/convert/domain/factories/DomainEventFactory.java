package com.chozoi.convertdata.convert.domain.factories;

import com.chozoi.convertdata.convert.domain.entities.DomainEvent;
import com.chozoi.convertdata.convert.domain.entities.EventContent;
import com.chozoi.convertdata.processors.product.values.ProductEventContent;
import com.chozoi.convertdata.utils.MessagePack;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DomainEventFactory {
  private static final String PRODUCT_AGGREGATE = "Product";
  private static final Short VERSION = 1;


  public final DomainEvent productCreated(ProductEventContent content) {
    byte[] arr = MessagePack.objectToBytea(content);
    ByteBuffer buffer = ByteBuffer.wrap(arr);
    return createDomainEvent(buffer, DomainEvent.EventType.ProductCreated);
  }

  private DomainEvent createDomainEvent(ByteBuffer content, DomainEvent.EventType type) {
    DomainEvent domainEvent = new DomainEvent();
    domainEvent.setId(UUID.randomUUID());
    domainEvent.setAggregate(PRODUCT_AGGREGATE);
    domainEvent.setVersion(VERSION);
    domainEvent.setContent(content);
    domainEvent.setCreatedAt(LocalDateTime.now());
    domainEvent.setType(type);
    return domainEvent;
  }
}
