package com.tunnelnetwork.KpOnlineStore.Models;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Receipt {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receipt_generator")
  @SequenceGenerator(name="receipt_generator", sequenceName = "receipt_seq")
  @Column(name = "id", updatable = false, nullable = false)   
  private long id;

  private String receiptOwner;

  @Embedded
  @ElementCollection
  private List<Voucher> voucherList = new ArrayList<Voucher>();
  
  @ManyToMany
  @JoinTable(name="tbl_product",
  joinColumns=@JoinColumn(name="receiptId"),
  inverseJoinColumns=@JoinColumn(name="productId")
  )
  private List<Product> productList = new ArrayList<Product>();

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime createdAt;

  @Transient
  public int getNumberOfProducts() {
    int count = 0;
    
    List<Product> products = getProductList();

    for (Product product : products) {
      count += (1 * product.getQuantity());
    }

    return count;
  }

  @Transient
  public Double getTotalOrderPrice() {
    double sum = 0D;
    List<Product> products = getProductList();

    for (Product product : products) {
      sum += (product.getPrice() * product.getQuantity());
    }

    return sum;
  }
}