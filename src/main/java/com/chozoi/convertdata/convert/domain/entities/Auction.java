package com.chozoi.convertdata.convert.domain.entities;

import com.chozoi.convertdata.processors.product.values.ProductAuctionState;
import com.chozoi.convertdata.utils.PostgreSQLEnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "auction", schema = "auctions")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Auction {

  @Id private Long id;

  @OneToOne
  @JoinColumn(name = "id")
  @JsonIgnore
  @MapsId
  private Product product;

  @Enumerated(value = EnumType.STRING)
  @Type(type = "pgsql_enum")
  @Column(name = "state")
  private ProductAuctionState state;

  @Column(name = "time_start")
  private LocalDateTime timeStart;

  @Column(name = "time_end")
  private LocalDateTime timeEnd;

  @Column(name = "price_step")
  private long priceStep;

  @Column(name = "start_price")
  private long startPrice;

  @Column(name = "buy_now_price")
  private Long buyNowPrice;

  @Column(name = "original_price")
  private long originalPrice;

  @Column(name = "time_duration")
  private Integer timeDuration;

  @OneToOne(mappedBy = "auction")
  @JoinColumn(name = "id", insertable = false, updatable = false)
  private AuctionResult result;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
