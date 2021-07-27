package com.tunnelnetwork.KpOnlineStore.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.tunnelnetwork.KpOnlineStore.Models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController extends CommonController{
  
  @GetMapping("/")
  private String indexPage(Model model) {

    if (isThereLoggedInUser()) {
      createCartAndVoucher(model);
    }

    getNumberOfProductsInCart(model);

    getUserRole(model);

    getUserFirstAndLastName(model);

    List<Product> preorderList = productRepository.getProductsByStatus("preorder");
    List<Product> sortedListByCreatedAt = productRepository.getProductsByCreatedAt();

    model.addAttribute("bestSellers", productRepository.getProductsByBestSeller(productRepository.getAllProducts())); 
    model.addAttribute("sortedByCreatedAtProducts", sortedListByCreatedAt.stream().limit(6).collect(Collectors.toList()));
    model.addAttribute("preorderProducts", preorderList.stream().limit(6).collect(Collectors.toList()));

    return "index";
  }
}
