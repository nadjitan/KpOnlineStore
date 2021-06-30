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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    if (cartService.getCartOfUser() != null) {
      model.addAttribute("cart", cartService.getCartOfUser());
    } else {
      Cart cart = new Cart();
      cart.setCartOwner(authentication.getName());
      cart.setCreatedAt(LocalDateTime.now());
      cart.setUpdatedAt(LocalDateTime.now());

      cartService.save(cart);

      model.addAttribute("cart", cartService.getCartOfUser());
    }
     
    return "index";
  }

  // Shopping cart buttons logic
  @RequestMapping(value = "/addproduct", method=RequestMethod.POST)
  @ResponseBody
  public String addProduct(@RequestParam("add") String id) {
    long productId = Integer.valueOf(id);
    cartService.addToCart(productService.getProduct(productId));
    
    return "user cart: " + cartService.getCartOfUser();
  }
  @RequestMapping(value=" ", params={"removeProduct"})
  public String removeProduct(HttpServletRequest req){

    return "index";
  }
}
