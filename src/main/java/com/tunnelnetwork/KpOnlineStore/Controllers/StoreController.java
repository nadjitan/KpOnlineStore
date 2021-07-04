package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreController extends CommonController{
  
  @GetMapping("/store")
  public String products(Model model) {
    if (!isThereLoggedInUser()) {
      return "redirect:/";
    }

    // Get products from database/json file
    model.addAttribute("products", productService.getAllProducts());
    // Cart for user
    createCartAndVoucher(model);
     
    return "store";
  }
}
