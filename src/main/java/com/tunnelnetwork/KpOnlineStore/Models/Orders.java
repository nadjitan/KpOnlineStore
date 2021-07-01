package com.tunnelnetwork.KpOnlineStore.Models;

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
public class Orders {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_generator")
  @SequenceGenerator(name="orders_generator", sequenceName = "orders_seq")
  @Column(name = "id", updatable = false, nullable = false)   
  private long id;

  private String ordersOwner;
  @Embedded
  @ElementCollection
  private List<Receipt> receipts = new ArrayList<Receipt>();

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime createdAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime updatedAt;
}

