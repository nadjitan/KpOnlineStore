package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.util.Optional;

import com.tunnelnetwork.KpOnlineStore.Models.Cart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController extends CommonController{

  @GetMapping("/cart/home")
  private String cartHomePage(Model model, @RequestParam("useVoucher") Optional<Integer> check) {
    if (!isThereLoggedInUser() || cartRepository.getCartOfUser() == null) {
      return "redirect:/login";
    }

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    Cart cart = cartRepository.getCartOfUser();

    if (check.isPresent()) {
      cart.setUseVoucher(1);

      model.addAttribute("checboxStatus", true);

      cartRepository.saveAndFlush(cart);
    }
    else {
      cart.setUseVoucher(0);
    }


    model.addAttribute("cart", cart);

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
