package com.tunnelnetwork.KpOnlineStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tunnelnetwork.KpOnlineStore.Models.Comment;

/**
 * Make a comment object out of json data.
 */
public class CommentDeserializer extends JsonDeserializer<List<Comment>> {
  
  @Override
  public List<Comment> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
      final ObjectCodec objectCodec = p.getCodec();
      final JsonNode listOrObjectNode = objectCodec.readTree(p);
      final List<Comment> result = new ArrayList<Comment>();
      
      if (listOrObjectNode.isArray()) {
          for (JsonNode node : listOrObjectNode) {
            result.add(objectCodec.treeToValue(node, Comment.class));
          }
      } else {
        result.add(objectCodec.treeToValue(listOrObjectNode, Comment.class));
      }

      return result;
  }
}