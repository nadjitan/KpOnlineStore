package com.tunnelnetwork.KpOnlineStore.Service;

import java.util.ArrayList;
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

  /**
   * Get cart of currently logged in user
   * 
   * @returns Cart
   */
  @Override
  public Cart getCartOfUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return cartRepository.findByCartOwner(authentication.getName());
  }

  @Override
  public Cart save(Cart cart) {
    return cartRepository.saveAndFlush(cart);
  }

  @Override
  public Cart addToCart(Product product, Integer productQuantity) {
    Cart cart = getCartOfUser();
    List<Product> currProducts = new ArrayList<Product>();
    product.setQuantity(productQuantity);
    
    if (cart.getCartProducts() == null) {
      currProducts.add(product);
    } else {
      for (Product cartProduct : cart.getCartProducts()) {
        currProducts.add(cartProduct);
      }

      currProducts.add(product);
    }
    
    cart.setCartProducts(currProducts);

    return cartRepository.saveAndFlush(cart);
  }

  @Override
  public boolean isProductInCart(long id) {
    Cart cart = getCartOfUser();

    for (Product cartProduct : cart.getCartProducts()) {
      if (cartProduct.getId() == id) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void removeProduct(long id) {
    Cart cart = getCartOfUser();

    for (Product cartProduct : cart.getCartProducts()) {
      if (cartProduct.getId() == id) {
        cart.getCartProducts().remove(cartProduct);
        cartRepository.saveAndFlush(cart);

        break;
      }
    }
  }
}
