package com.tunnelnetwork.KpOnlineStore.Controllers;

import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.DAO.CartRepository;
import com.tunnelnetwork.KpOnlineStore.Models.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartRestController {
  
  @Autowired
  private CartRepository cartRepository;

  @GetMapping("/{id}")
  private Cart findCart(@PathVariable("id") long id) {
    return cartRepository.getCart(id);
  }

  @GetMapping("/all")
  private @NotNull Iterable<Cart> list() {
    return cartRepository.getAllCarts();
  }
}
