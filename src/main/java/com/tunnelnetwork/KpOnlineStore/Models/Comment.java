package com.tunnelnetwork.KpOnlineStore.Models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
public class Comment {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_generator")
  @SequenceGenerator(name="comment_generator", sequenceName = "comment_seq")
  @Column(name = "id", updatable = false, nullable = false)
  private long id;

  private long commentUserId;
  private String userName;
  private String userComment;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;
}
