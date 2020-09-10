package com.chozoi.convertdata.processors.product.ShopStreams;

import chozoi.products.domain_event.Key;
import chozoi.products.domain_event.Value;
import chozoi.products.stats.ShopProductStats;
import com.chozoi.convertdata.processors.product.TopicConfig;
import com.chozoi.convertdata.processors.product.values.ProductEventContent;
import com.chozoi.convertdata.processors.product.values.ProductState;
import com.chozoi.convertdata.processors.product.values.ProductState.*;
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
public class ShopProductStreams extends BaseStreams{

  public static final String SHOP_PRODUCT_STATS_STORE = "shop.products.stats.store";

  @Bean
  public KStream<Integer, ProductEventContent> shopProductStateStream(
      @Qualifier("productStateEventStream") KStream<Key, Value> productStateEventStream) {

    return productStateEventStream
        .mapValues(this::eventContentToObject)
        .filter(
            (key, shopProductContent) -> {
              Map<String, Object> map = objectToMap(shopProductContent);
              if (map!=null && map.get("auction") == null) {
                log.info("chay vao null auction");
                log.info(map.get("productId") + "==============>" + map.toString());
                return true;
              } else {
                log.info("chay vao auction");
                log.info(map.get("productId") + "==============>" + map.toString());
                return false;
              }
            })
        .selectKey((key, value) -> value.getShopId());
  }

  @Bean
  public KStream<Integer, ProductEventContent> shopProductStatsStream(
      @Qualifier("shopProductStateStream")
              KStream<Integer, ProductEventContent> shopProductStateStream) {

    KTable<Integer, ShopProductStats> longTermStats =
        shopProductStateStream
            .groupByKey(
                Serialized.with(Serdes.Integer(), CustomSerdes.json(ProductEventContent.class)))
            .aggregate(
                this::emptyStats,
                this::aggregateShopProduct,
                Materialized.<Integer, ShopProductStats, KeyValueStore<Bytes, byte[]>>as(
                    SHOP_PRODUCT_STATS_STORE)
                    .withKeySerde(Serdes.Integer()));
    longTermStats
        .toStream()
        .peek(
            (key, value) -> {
              log.debug(
                  "SHOP_PRODUCT_STATS_TOPIC:" + key + " -------> " + value);
            })
        .selectKey((key, value) -> key.toString())
        .to(TopicConfig.SHOP_PRODUCT_STATS_TOPIC, Produced.keySerde(Serdes.String()));

    return shopProductStateStream;
  }

  private ShopProductStats emptyStats() {
    return ShopProductStats.newBuilder().build();
  }

  private ShopProductStats aggregateShopProduct(
      Integer shopId, ProductEventContent newProductEvent, ShopProductStats currentStats) {
    ShopProductStats.Builder statsBuilder = ShopProductStats.newBuilder(currentStats);
    statsBuilder.setShopId(shopId);
    Map<String, Object> map = objectToMap(newProductEvent);
    String state = map.get("state") != null ? String.valueOf(map.get("state")) : null;

    if (!state.equals(String.valueOf(ProductState.DELETED))) {
      statsBuilder.setCountProduct(currentStats.getCountProduct() + 1);
    }
    addCountByState(statsBuilder, state, currentStats);
    return statsBuilder.build();
  }

  private void addCountByState(ShopProductStats.Builder statsBuilder, String state, ShopProductStats currentStats) {
    switch (state) {
      case "DRAFT":
        statsBuilder.setCountDraftProduct(currentStats.getCountDraftProduct() + 1);
        break;
      case "PENDING":
        statsBuilder.setCountPendingProduct(currentStats.getCountPendingProduct() + 1);
        break;
      case "READY":
        statsBuilder.setCountReadyProduct(currentStats.getCountReadyProduct() + 1);
        break;
      case "PUBLIC":
        statsBuilder.setCountPublicProduct(currentStats.getCountPublicProduct() + 1);
        break;
      case "REJECT":
        statsBuilder.setCountRejectProduct(currentStats.getCountRejectProduct() + 1);
        break;
      case "REPORT":
        statsBuilder.setCountReportProduct(currentStats.getCountReportProduct() + 1);
        break;
      case "DELETED":
        statsBuilder.setCountDeletedProduct(currentStats.getCountDeletedProduct() + 1);
        break;
    }
  }

  private ProductEventContent eventContentToObject(Value event) {
    ByteBuffer buf = event.getContent();
    byte[] arr = buf.array();
    ProductEventContent productEventContent =
        MessagePack.byteaToObject(arr, ProductEventContent.class);
    productEventContent.setEventType(event.getType());
    return productEventContent;
  }
}
