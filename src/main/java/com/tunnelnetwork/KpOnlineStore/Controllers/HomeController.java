package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.tunnelnetwork.KpOnlineStore.DAO.SoldProductsSorter;
import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController extends CommonController{
  
  @GetMapping("/")
  private String indexPage(Model model) {

    if (hasRole("ADMIN")) {
      model.addAttribute("crud", true);
    }else {
      model.addAttribute("crud", false);
    }

    List<Product> productsInDB = productService.getAllProducts();
    productsInDB.sort(new SoldProductsSorter());
    
    List<Product> bestSellers = new ArrayList<Product>();

    for (int i = 0; i < 10; i++) {
      try {
        bestSellers.add(productsInDB.get(i));
      } catch (Exception e) {
        break;
      }
    }
    
    model.addAttribute("bestSellers", bestSellers);

    return "index";
  }
  
}
