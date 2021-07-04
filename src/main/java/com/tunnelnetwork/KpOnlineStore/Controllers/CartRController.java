package com.tunnelnetwork.KpOnlineStore.Controllers;

import javax.validation.constraints.NotNull;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartRController {
  
  @Autowired
  private CartService cartService;

  @GetMapping("/{id}")
  public Cart findCart(@PathVariable("id") long id) {
    return cartService.getCart(id);
  }

  @GetMapping("/all")
  public @NotNull Iterable<Cart> list() {
    return cartService.getAllCarts();
  }
}
