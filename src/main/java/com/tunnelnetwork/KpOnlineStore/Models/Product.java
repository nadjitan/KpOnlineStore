package com.tunnelnetwork.KpOnlineStore.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tunnelnetwork.KpOnlineStore.CommentDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
  @SequenceGenerator(name="product_generator", sequenceName = "product_seq")
  private long id;
  
  private String productName;
  private String description;
  private String category;
  private double price;
  private String status;
  private String image;
  private Integer rating;
  private String[] tags;

  @Lob
  private Integer quantity;

  @Embedded
  @JsonDeserialize(using = CommentDeserializer.class)
  private List<Comment> comments = new ArrayList<Comment>();

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;
}
