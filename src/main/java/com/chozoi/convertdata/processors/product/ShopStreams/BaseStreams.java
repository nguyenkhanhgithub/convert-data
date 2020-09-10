package com.chozoi.convertdata.processors.product.ShopStreams;

import com.chozoi.convertdata.processors.product.values.ProductEventContent;
import com.chozoi.convertdata.processors.product.values.ProductState;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;

public class BaseStreams {
  protected boolean shouldInState(String state, ProductState checkState) {
    return Objects.equals(state, String.valueOf(checkState));
  }

  protected Map<String, Object> objectToMap(ProductEventContent newProductEvent) {
    ObjectMapper oMapper = new ObjectMapper();
    Object object = newProductEvent.getData();
    return oMapper.convertValue(object, Map.class);
  }
}
