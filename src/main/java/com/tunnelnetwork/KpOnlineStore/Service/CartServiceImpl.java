package com.tunnelnetwork.KpOnlineStore.Service;

import com.tunnelnetwork.KpOnlineStore.DAO.CartRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
  
  @Autowired
  private CartRepository cartRepository;

  @Override
  public Cart getCart(long id) {
    return cartRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
  }

  @Override
  public Cart save(Cart cart) {
    return cartRepository.save(cart);
  }
}
