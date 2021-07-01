package com.tunnelnetwork.KpOnlineStore.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
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

@Embeddable
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

  @ElementCollection
  private List<Integer> quantityList;
  @Embedded
  @ElementCollection
  private List<Product> products = new ArrayList<Product>();

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime createdAt;
}