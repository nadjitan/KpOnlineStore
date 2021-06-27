package com.tunnelnetwork.KpOnlineStore.Controllers;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Service.CartService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
  
  private CartService cartService;

  public CartController(CartService cartService) {
      this.cartService = cartService;
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Cart list(@RequestParam long id) {
      return cartService.getCart(id);
  }
}
