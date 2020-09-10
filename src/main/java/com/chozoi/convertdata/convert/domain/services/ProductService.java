package com.chozoi.convertdata.convert.domain.services;

import chozoi.products.domain_event.Value;
import com.chozoi.convertdata.convert.domain.entities.DomainEvent;
import com.chozoi.convertdata.convert.domain.entities.Product;
import com.chozoi.convertdata.processors.product.values.ProductEventContent;
import com.chozoi.convertdata.processors.product.values.ProductType;
import com.chozoi.convertdata.utils.MessagePack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.*;

@Service
@Slf4j
public class ProductService extends BaseService {
  public Object getAllProduct(Pageable pageable) {
    Page<Product> page = productRepository.findProductByStateNotNull(pageable);
    List<Product> products = page.getContent();
    List<ProductEventContent> productEventContents = new ArrayList<>();
    for (Product product : products) {
      ProductEventContent productEventContent = new ProductEventContent();
      Map<Object, Object> object = new HashMap<>();
      object.put("state", product.getState());
      object.put("auction", product.getAuction());
      productEventContent.setData(object);
      productEventContent.setShopId(product.getShop().getId());
      productEventContents.add(productEventContent);
    }
    List<DomainEvent> domainEvents = new ArrayList<>();
    for (ProductEventContent productEventContent : productEventContents) {
      DomainEvent domainEvent = domainEventFactory.productCreated(productEventContent);
      domainEvents.add(domainEvent);
      productProducer.save(domainEvent);
    }
    return domainEvents;
  }

  public Object getProductAuction(List<Long> productIds) {
    List<Product> products = productRepository.findProductByIdIn(productIds);
    List<ProductEventContent> productEventContents = new ArrayList<>();
    for (Product product : products) {
      ProductEventContent productEventContent = new ProductEventContent();
      Map<Object, Object> object = new HashMap<>();
      object.put("state", product.getState());
      object.put("auction", product.getAuction());
      productEventContent.setData(object);
      productEventContent.setShopId(product.getShop().getId());
      productEventContents.add(productEventContent);
    }
    List<DomainEvent> domainEvents = new ArrayList<>();
    for (ProductEventContent productEventContent : productEventContents) {
      DomainEvent domainEvent = domainEventFactory.productCreated(productEventContent);
      domainEvents.add(domainEvent);
      productProducer.save(domainEvent);
    }
    return domainEvents;
  }

  public Object getShopProduct(Integer shop_id) {
    List<Product> products = productRepository.findProductByShopId(shop_id);
    List<ProductEventContent> productEventContents = new ArrayList<>();
    for (Product product : products) {
      ProductEventContent productEventContent = new ProductEventContent();
      Map<Object, Object> object = new HashMap<>();
      object.put("state", product.getState());
      object.put("auction", product.getAuction());
      productEventContent.setData(object);
      productEventContent.setShopId(product.getShop().getId());
      productEventContents.add(productEventContent);
    }
    List<DomainEvent> domainEvents = new ArrayList<>();
    for (ProductEventContent productEventContent : productEventContents) {
      DomainEvent domainEvent = domainEventFactory.productCreated(productEventContent);
      domainEvents.add(domainEvent);
      productProducer.save(domainEvent);
    }
    return domainEvents;
  }

  private byte[] eventContentToObject(ProductEventContent content) {
    return MessagePack.objectToBytea(content);
  }
}
