package com.chozoi.convertdata.processors.product;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

  public static final String PRODUCT_DOMAIN_EVENT_TOPIC = "chozoi.products.domain_event";

  public static final String SHOP_PRODUCT_SELLING_VARIANT_STATS_TOPIC = "shop.products.selling.variant.stats";

  public static final String SHOP_PRODUCT_SELLING_STATS_TOPIC = "shop.products.selling.stats";

  public static final String SHOP_PRODUCT_STATS_TOPIC = "shop.products.stats";


  @Value("${spring.kafka.topic.num-partitions}")
  private int numPartitions;

  @Value("${spring.kafka.topic.replication-factor}")
  private short replicationFactor;

  @Bean
  public NewTopic domainEventTopic() {
    return new NewTopic(PRODUCT_DOMAIN_EVENT_TOPIC, numPartitions, replicationFactor);
  }

  @Bean
  public NewTopic shopProductTopic() {
    return new NewTopic(SHOP_PRODUCT_STATS_TOPIC, numPartitions, replicationFactor);
  }

  @Bean
  public NewTopic shopProductSellingVariantTopic() {
    return new NewTopic(SHOP_PRODUCT_SELLING_VARIANT_STATS_TOPIC, numPartitions, replicationFactor);
  }

  @Bean
  public NewTopic shopProductSellingTopic() {
    return new NewTopic(SHOP_PRODUCT_SELLING_STATS_TOPIC, numPartitions, replicationFactor);
  }

}
