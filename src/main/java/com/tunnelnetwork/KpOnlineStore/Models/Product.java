package com.tunnelnetwork.KpOnlineStore.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
  
  @NotBlank
  private String productName;

  @Lob
  @NotBlank
  private String description;

  @NotBlank
  private String category;

  @NotNull
  private Double price;

  @NotBlank
  private String status;

  @NotBlank
  private String image;

  private String[] tags;

  private Integer rating;

  private Integer numberOfSold;
  
  @Lob
  private Integer quantity;

  @Embedded
  @ElementCollection
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name="product_id")
  @JsonDeserialize(using = CommentDeserializer.class)
  private List<Comment> comments = new ArrayList<Comment>();

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;
}
