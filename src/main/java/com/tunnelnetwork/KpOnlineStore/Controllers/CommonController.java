package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;
import com.tunnelnetwork.KpOnlineStore.Service.CartService;
import com.tunnelnetwork.KpOnlineStore.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {

  @Autowired
  private ProductService productService;

  @Autowired
  private CartService cartService;

  @RequestMapping("/")
  public String products(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Get products from database/json file
    model.addAttribute("products", productService.getAllProducts());

    // Get cart but if there is none create new cart for new user
    if (cartService.findCartOwner(authentication.getName()) != null) {
      model.addAttribute("cart", cartService.findCartOwner(authentication.getName()));
    } else {
      Cart cart = new Cart();
      cart.setCartOwner(authentication.getName());
      cart.setCreatedAt(LocalDateTime.now());
      cart.setUpdatedAt(LocalDateTime.now());

      cartService.save(cart);

      model.addAttribute("cart", cartService.findCartOwner(authentication.getName()));
    }
     
    return "index";
  }

  // Shopping cart buttons logic
  @RequestMapping(value="/productmngr", params={"addProduct"})
  public String addProduct(final CartService cartService) {

    return "index";
  }
  @RequestMapping(value="/productmngr", params={"removeProduct"})
  public String removeProduct(final CartService cartService, final HttpServletRequest req){

    return "index";
  }
}
