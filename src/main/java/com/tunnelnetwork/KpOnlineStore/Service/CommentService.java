package com.tunnelnetwork.KpOnlineStore.Service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Comment;

import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface CommentService {
  
  Comment getComment(@Min(value = 1L, message = "Invalid comment ID.") long id);

  List<Comment> save(@NotNull(message = "The comments cannot be null.") @Valid List<Comment> comments);

  Comment save(@NotNull(message = "The comments cannot be null.") @Valid Comment comment);
}
