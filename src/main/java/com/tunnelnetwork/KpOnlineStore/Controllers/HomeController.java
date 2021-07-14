package com.tunnelnetwork.KpOnlineStore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController extends CommonController{
  
  @GetMapping("/")
  private String indexPage(Model model) {

    getUserRole(model);

    getUserFirstAndLastName(model);
    
    model.addAttribute("bestSellers", productRepository.getProductsByBestSeller(productRepository.getAllProducts()));

    return "index";
  }
}
