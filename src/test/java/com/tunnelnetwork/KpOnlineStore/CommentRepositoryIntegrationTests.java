package com.tunnelnetwork.KpOnlineStore;

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

    Comment comment = testEntityManager.find(Comment.class, commentId);
    Iterable<Comment> commentList = commentRepository.getAllCommentsByUsername(comment.getUserName());

    assertEquals(StreamSupport.stream(commentList.spliterator(), false).count(), 1);
  }

  @Test
  public void whenInvalidUserName_thenCommentShouldNotBeFound() {

    String username = "notregisteredmail@mail.com";
    Iterable<Comment> commentList = commentRepository.getAllCommentsByUsername(username);

    assertEquals(StreamSupport.stream(commentList.spliterator(), false).count(), 0);
  }

  @Test
  public void whenFindById_thenCommentShouldBeFound() {

    Optional<Comment> comment = commentRepository.findById(commentId);

    assertTrue(comment.isPresent());
  }

  @Test
  public void whenInvalidId_thenCommentShouldNotBeFound() {

    long id = -100;
    Optional<Comment> comment = commentRepository.findById(id);

    assertFalse(comment.isPresent());
  }
}
