package com.tunnelnetwork.KpOnlineStore.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Product {
  
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private long id;

  // So that Jackson knows the name of the JSON field to map
  @JsonProperty("name")
  private String name;

  @JsonProperty("category")
  private String category;

  @JsonProperty("price")
  private double price;
}
