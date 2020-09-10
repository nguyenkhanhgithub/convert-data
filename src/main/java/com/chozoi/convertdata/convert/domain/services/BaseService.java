package com.chozoi.convertdata.convert.domain.services;

import com.chozoi.convertdata.convert.domain.factories.DomainEventFactory;
import com.chozoi.convertdata.convert.domain.factories.ModelMapper;
import com.chozoi.convertdata.convert.domain.producers.ProductProducer;
import com.chozoi.convertdata.convert.domain.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {
  @Autowired protected ProductRepository productRepository;
  @Autowired protected DomainEventFactory domainEventFactory;
  @Autowired protected ProductProducer productProducer;
}
