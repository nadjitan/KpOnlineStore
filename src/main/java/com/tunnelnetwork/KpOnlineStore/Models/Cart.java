package com.tunnelnetwork.KpOnlineStore.Models;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cart {
  
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private long id;

  @Getter
  @Setter
  @JsonFormat(pattern = "dd/MM/yyyy") 
  private LocalDate dateCreated;

  @Getter
  @Setter
  @Valid
  @Embedded
  private List<Product> cartProducts;

  @Transient
  public Double getTotalOrderPrice() {
    double sum = 0D;
    List<Product> cartProducts = getCartProducts();

    for (Product cp : cartProducts) {
        sum += cp.getPrice();
    }

    return sum;
  }

  @Transient
  public int getNumberOfProducts() {
    return this.cartProducts.size();
  }
}
