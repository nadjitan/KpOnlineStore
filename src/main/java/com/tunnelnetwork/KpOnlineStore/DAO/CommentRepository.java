package com.tunnelnetwork.KpOnlineStore.DAO;

import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByUserName(String username);

  default Comment getComment(long id) {
    return this
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
  }

  default List<Comment> getAllCommentsByUsername(String username) {
    return this
      .findAllByUserName(username);
  }
}
