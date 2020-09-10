package com.chozoi.convertdata.processors.product.values;

import com.chozoi.convertdata.convert.domain.entities.EventContent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProductEventContent extends EventContent {
  public Long productId;
  public Integer shopId;
  public Integer updatedById;
  public Integer updatedBySystemId;
  public Object data;
  private String eventType;
}
