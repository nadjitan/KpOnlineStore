package com.tunnelnetwork.KpOnlineStore.Service;

import javax.transaction.Transactional;

import com.tunnelnetwork.KpOnlineStore.DAO.CommentRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public Comment getComment(long id) {
    return commentRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
  }

  @Override
  public List<Comment> save(List<Comment> comments) {
    return commentRepository.saveAllAndFlush(comments);
  }

  @Override
  public Comment save(Comment comment) {
    return commentRepository.saveAndFlush(comment);
  }
  
}
