package com.chozoi.convertdata.convert.domain.repositories;

import com.chozoi.convertdata.convert.domain.entities.Product;
import com.chozoi.convertdata.convert.domain.entities.types.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findProductByStateNotNull(Pageable pageable);

  Product findProductById(Long id);

  List<Product> findProductByIdIn(List<Long> productIds);
  
  List<Product> findProductByShopId(Integer shop_id);
}
