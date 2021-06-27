package com.tunnelnetwork.KpOnlineStore.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Cart {
  
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private long id;

  
}
