package com.tunnelnetwork.KpOnlineStore.Models;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Product {
  
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private long id;
  
  private String name;
  private String category;
  private double price;
}
