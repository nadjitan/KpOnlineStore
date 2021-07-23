package com.tunnelnetwork.KpOnlineStore.RepositoryTests;

import com.tunnelnetwork.KpOnlineStore.DAO.CommentRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.stream.StreamSupport;

@DataJpaTest
public class CommentRepositoryIntegrationTests {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private long commentId;
  
  @BeforeEach
  public void setup() {

    Comment comment = new Comment();
    comment.setCommentUserId((long) 1);
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());
    comment.setUserComment("Testing comment repo");
    comment.setUserName("test@mail.com");

    commentId = testEntityManager.persistAndFlush(comment).getId();
  }

  @Test
  public void whenFindByUserName_thenCommentShouldBeFound() {

    // Given
    Comment comment = testEntityManager.find(Comment.class, commentId);

    // When
    Iterable<Comment> commentList = commentRepository.getAllCommentsByUsername(comment.getUserName());

    // Then
    assertEquals(StreamSupport.stream(commentList.spliterator(), false).count(), 1);
  }

  @Test
  public void whenInvalidUserName_thenCommentShouldNotBeFound() {

    // Given
    String username = "notregisteredmail@mail.com";

    // When
    Iterable<Comment> commentList = commentRepository.getAllCommentsByUsername(username);

    // Then
    assertEquals(StreamSupport.stream(commentList.spliterator(), false).count(), 0);
  }

  @Test
  public void whenFindById_thenCommentShouldBeFound() {

    // When
    Optional<Comment> comment = commentRepository.findById(commentId);

    // Then
    assertTrue(comment.isPresent());
  }

  @Test
  public void whenInvalidId_thenCommentShouldNotBeFound() {

    // Given
    long id = -100;

    // When
    Optional<Comment> comment = commentRepository.findById(id);

    // Then
    assertFalse(comment.isPresent());
  }
}
