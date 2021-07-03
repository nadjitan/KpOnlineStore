package com.tunnelnetwork.KpOnlineStore.Models;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Voucher {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voucher_generator")
  @SequenceGenerator(name="voucher_generator", sequenceName = "voucher_seq")
  @Column(name = "id", updatable = false, nullable = false)   
  private long id;

  private String voucherName;
  @ElementCollection
  private List<String> userList = new ArrayList<String>();
  private String description;

  @Digits(integer=5, fraction=2)
  private int discount;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime createdAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime updatedAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
  private LocalDateTime expiryDate;

  @Transient
  public boolean isUserInList(String username) {
    return userList.contains(username);
  }

  @Transient
  public void addUserInList(String username) {
    userList.add(username);
  }

  @Transient
  public void removeUserInList(String username) {
    userList.remove(username);
  }
}
