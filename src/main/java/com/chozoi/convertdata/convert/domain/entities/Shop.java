package com.chozoi.convertdata.convert.domain.entities;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "shop_view", schema = "products")
@Immutable
@Data
@NoArgsConstructor
public class Shop {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
