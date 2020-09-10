package com.chozoi.convertdata.convert.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(schema = "products", name = "category")
@Where(clause = "state = 'PUBLIC'")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
}
