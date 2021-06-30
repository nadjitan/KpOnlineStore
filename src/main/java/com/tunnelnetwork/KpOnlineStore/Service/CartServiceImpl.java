package com.tunnelnetwork.KpOnlineStore.Service;

import java.util.List;

import com.tunnelnetwork.KpOnlineStore.DAO.CartRepository;
import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
  
  @Autowired
  private CartRepository cartRepository;

  @Override
  public Iterable<Cart> getAllCarts() {
    return cartRepository.findAll();
  }

  @Override
  public Cart getCart(long id) {
    return cartRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
  }

  @Override
  public Cart findCartOwner(String username) {
    return cartRepository.findByCartOwner(username);
}

  @Override
  public Cart save(Cart cart) {
    return cartRepository.save(cart);
  }

  /**
   * Get cart of currently logged in user
   * 
   * @returns Cart
   */
  @Override
  public Cart getCartOfUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return findCartOwner(authentication.getName());
  }

  @Override
  public void addToCart(Product product) {
    try {
      Cart cart = getCartOfUser();
      cart.getCartProducts().add(product);

      cartRepository.save(cart);

      System.out.println("Product added to user's cart!");
    } catch (Exception e) {
      System.out.println("Product not added to user's cart:" + e);
    }
  }
}
