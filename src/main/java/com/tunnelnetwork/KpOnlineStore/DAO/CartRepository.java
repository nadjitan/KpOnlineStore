package com.tunnelnetwork.KpOnlineStore.DAO;

import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.Exceptions.ResourceNotFoundException;
import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Models.Product;
import com.tunnelnetwork.KpOnlineStore.Models.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Cart findByCartOwner(String userDetails);
  
  default Iterable<Cart> getAllCarts() {
    return this.findAll();
  }
  
  default Cart getCart(long id) {
    return this
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
  }

  /**
   * Get cart of currently logged in user
   * 
   * @returns Cart
   */
  default Cart getCartOfUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return this.findByCartOwner(authentication.getName());
  }
  
  default Cart addToCart(Product product, Integer productQuantity) {
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

    return this.saveAndFlush(cart);
  }
  
  default boolean isProductInCart(long id) {
    Cart cart = getCartOfUser();

    for (Product cartProduct : cart.getCartProducts()) {
      if (cartProduct.getId() == id) {
        return true;
      }
    }

    return false;
  }
  
  default void removeProduct(long id) {
    Cart cart = getCartOfUser();

    for (Product cartProduct : cart.getCartProducts()) {
      if (cartProduct.getId() == id) {
        cart.getCartProducts().remove(cartProduct);
        this.saveAndFlush(cart);

        break;
      }
    }
  }
  
  default void removeProducts(String username) {
    Cart cart = getCartOfUser();
    List<Product> productList = new ArrayList<Product>();

    cart.setCartProducts(productList);

    this.saveAndFlush(cart);
  }
  
  default void removeVouchers(String username) {
    Cart cart = getCartOfUser();
    List<Voucher> voucherList = new ArrayList<Voucher>();

    cart.setVouchers(voucherList);

    this.saveAndFlush(cart);
  }
}
