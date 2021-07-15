package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController extends CommonController{

  @GetMapping("/cart/home")
  private String cartHomePage(Model model) {
    getUserRole(model);

    getUserFirstAndLastName(model);
    
    if (!isThereLoggedInUser() || cartRepository.getCartOfUser() == null) {
      return "redirect:/login";
    }

    model.addAttribute("cart", cartRepository.getCartOfUser());

    return "cart";
  }

  // Shopping cart buttons logic
  @PostMapping("/addProduct")
  private String addProduct(@RequestParam("productId") long id, @RequestParam("productQuantity") Integer productQuantity) {
    if (!isThereLoggedInUser()) {
      return "redirect:/login";
    }
    
    if (!cartRepository.isProductInCart(id)) {
      cartRepository.addToCart(productRepository.getProduct(id), productQuantity);
    }

    return "redirect:/store/1";
  }
  @PostMapping("/removeProduct")
  private String removeProduct(@RequestParam("productId") long id){
    if (cartRepository.isProductInCart(id)) {
      cartRepository.removeProduct(id);
    }

    return "redirect:/cart/home";
  }
}
