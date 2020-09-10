package com.chozoi.convertdata.processors.product.ShopStreams;

import chozoi.products.domain_event.Key;
import chozoi.products.domain_event.Value;
import chozoi.products.stats.ShopProductSellingStats;
import com.chozoi.convertdata.processors.product.TopicConfig;
import com.chozoi.convertdata.processors.product.values.ProductEventContent;
import com.chozoi.convertdata.processors.product.values.ProductState;
import com.chozoi.convertdata.utils.CustomSerdes;
import com.chozoi.convertdata.utils.EventContentUtils;
import com.chozoi.convertdata.utils.MessagePack;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Configuration
public class ShopProductSellingStreams extends BaseStreams {

  public static final String SHOP_PRODUCT_SELLING_STATS_STORE = "shop.products.selling.stats.store";

  @Bean
  public KStream<Integer, ProductEventContent> shopProductSellingStateStream(
      @Qualifier("productStateEventStream") KStream<Key, Value> productStateEventStream) {

    return productStateEventStream
        .mapValues(this::eventContentToObject)
        .filter(
            (key, shopProductContent) -> {
              Map<String, Object> map = objectToMap(shopProductContent);
              if (map!=null && map.get("auction") == null) {
                log.info("chay vao null auction");
                return true;
              } else {
                log.info("chay vao auction");
                return false;
              }
            })
        .selectKey((key, value) -> value.getShopId());
  }

  @Bean
  public KStream<Integer, ProductEventContent> shopProductSellingStatsStream(
      @Qualifier("shopProductSellingStateStream")
          KStream<Integer, ProductEventContent> shopProductSellingStateStream) {

    KTable<Integer, ShopProductSellingStats> longTermStats =
        shopProductSellingStateStream
            .groupByKey(
                Serialized.with(Serdes.Integer(), CustomSerdes.json(ProductEventContent.class)))
            .aggregate(
                this::emptyStats,
                this::aggregateShopProductSelling,
                Materialized.<Integer, ShopProductSellingStats, KeyValueStore<Bytes, byte[]>>as(
                        SHOP_PRODUCT_SELLING_STATS_STORE)
                    .withKeySerde(Serdes.Integer()));
    longTermStats
        .toStream()
        .peek(
            (key, value) -> {
              log.debug("SHOP_PRODUCT_SELLING_STATS_TOPIC:" + key + " -------> " + value);
            })
        .to(TopicConfig.SHOP_PRODUCT_SELLING_STATS_TOPIC, Produced.keySerde(Serdes.Integer()));

    return shopProductSellingStateStream;
  }

  private ShopProductSellingStats emptyStats() {
    return ShopProductSellingStats.newBuilder().build();
  }

  private ShopProductSellingStats aggregateShopProductSelling(
      Integer shopId, ProductEventContent newProductEvent, ShopProductSellingStats currentStats) {
    ShopProductSellingStats.Builder statsBuilder = ShopProductSellingStats.newBuilder(currentStats);
    statsBuilder.setShopId(shopId);
    Map<String, Object> map = objectToMap(newProductEvent);
    String state = map.get("state") != null ? String.valueOf(map.get("state")) : null;
    if (shouldInState(state, ProductState.PUBLIC)) {
      statsBuilder.setCountProduct(statsBuilder.getCountProduct() + 1);
    }
    return statsBuilder.build();
  }

  private ProductEventContent eventContentToObject(Value event) {
    ByteBuffer buf = event.getContent();
    byte[] arr = buf.array();
    return MessagePack.byteaToObject(arr, ProductEventContent.class);
  }
}
