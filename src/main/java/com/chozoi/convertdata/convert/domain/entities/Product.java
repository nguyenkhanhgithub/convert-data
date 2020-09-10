package com.chozoi.convertdata.convert.domain.entities;

import com.chozoi.convertdata.processors.product.values.ProductState;
import com.chozoi.convertdata.processors.product.values.ProductType;
import com.chozoi.convertdata.utils.GenericArrayUserType;
import com.chozoi.convertdata.utils.PostgreSQLEnumType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Log4j2
@Entity
@NoArgsConstructor
@Table(name = "product", schema = "products")
@TypeDef(name = "pg-enum", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "pg-jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "pg-array", typeClass = GenericArrayUserType.class)
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Type(type = "pg-enum")
  @Column(nullable = false, columnDefinition = "product_type", name = "type")
  private ProductType type;

  @Enumerated(value = EnumType.STRING)
  @Type(type = "pg-enum")
  @Column(nullable = false, columnDefinition = "product_state", name = "state")
  private ProductState state;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  // relation
  @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
  @JoinColumn(name = "id")
  private Auction auction;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<ProductVariant> variants;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Shop shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Category category;

}
