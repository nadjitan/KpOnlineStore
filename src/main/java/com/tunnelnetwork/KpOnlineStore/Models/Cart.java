package com.tunnelnetwork.KpOnlineStore.Models;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cart {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_generator")
  @SequenceGenerator(name="cart_generator", sequenceName = "cart_seq")
  @Column(name = "id", updatable = false, nullable = false)   
  private long id;

  private String cartOwner;

  @Embedded
  @ElementCollection
  private List<Product> cartProducts = new ArrayList<Product>();
  
  @Embedded
  @ElementCollection
  private List<Voucher> vouchers = new ArrayList<Voucher>();
  private int useVoucher = 0;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime createdAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime updatedAt;

  @Transient
  public Double getTotalOrderPrice() {
    double sum = 0D;
    List<Product> cartProducts = getCartProducts();

    for (Product product : cartProducts) {
      sum += (product.getPrice() * product.getQuantity());
    }

    return sum;
  }

  @Transient
  public double getDiscountedPrice() {
    double s = 0;
    double finalPrice = getTotalOrderPrice();

    for (Voucher voucher : vouchers) {
      s = 100 - voucher.getDiscount(); 
      finalPrice = (s * finalPrice) / 100;
    }

    return finalPrice;
  }

  @Transient
  public int getNumberOfProducts() {
    int count = 0;
    
    List<Product> cartProducts = getCartProducts();

    for (Product product : cartProducts) {
      count += (1 * product.getQuantity());
    }

    return count;
  }
}
