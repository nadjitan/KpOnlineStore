package com.tunnelnetwork.KpOnlineStore.DAO;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  default Comment getComment(long id) {
    return this
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
  }
}
