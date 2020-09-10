package com.chozoi.convertdata.convert.domain.entities;

import chozoi.products.domain_event.Key;
import chozoi.products.domain_event.Value;
import com.chozoi.convertdata.utils.JsonParser;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
public class DomainEvent {

  private UUID id;
  private Short version;
  private EventType type;
  private String aggregate;
  private ByteBuffer content;
  private LocalDateTime createdAt;

  public Value toValueAvro() throws IOException {
    Value value = new Value();
    value.setId(this.getId().toString());
    value.setVersion((int) this.getVersion());
    value.setType(this.getType().toString());
    value.setAggregate(this.getAggregate());
    value.setContent(this.getContent());
    value.setCreatedAt(createdAt.toInstant(ZoneOffset.UTC).toEpochMilli());
    return value;
  }

  public Key toKeyAvro() {
    return new Key(this.getId().toString());
  }

  public enum EventType {
    ProductCreated,
    ProductChangeStated,
    ProductViewed,
    InventoryCreated,
    InventoryQuantityChanged
  }
}
