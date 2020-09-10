package com.chozoi.convertdata.convert.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Data
@Entity
@Log4j2
@NoArgsConstructor
@Table(name = "product_variant", schema = "products")
public class ProductVariant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  @JsonIgnore
  private Product product;

  @Column(name = "price")
  private Long price;

  @Column(name = "sale_price")
  private Long salePrice;
}
